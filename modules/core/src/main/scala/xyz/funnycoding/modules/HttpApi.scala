package xyz.funnycoding.modules

import cats.effect._
import cats.implicits._
import org.http4s.server.middleware._

import scala.concurrent.duration._
import org.http4s._
import org.http4s.implicits._
import xyz.funnycoding.http.routes._

object HttpApi {
  def make[F[_]: Concurrent: Timer](algebras: Algebras[F]): F[HttpApi[F]] = Sync[F].delay {
    new HttpApi(algebras)
  }
}

class HttpApi[F[_]: Timer: Concurrent](algebras: Algebras[F]) {
  private val companiesRoute   = new CompaniesRoute[F](algebras.companies).routes
  private val employeesRoute   = new EmployeesRoute[F](algebras.employees).routes
  private val healthCheckRoute = new HealthCheckRoute[F](algebras.healthcheck).routes

  private val middleware: HttpRoutes[F] => HttpRoutes[F] = {
    { http: HttpRoutes[F] =>
      AutoSlash(http)
    } andThen { http: HttpRoutes[F] =>
      CORS.policy.withAllowOriginAll
        .withAllowCredentials(false)
        .apply(http)
    } andThen { http: HttpRoutes[F] =>
      Timeout(60.seconds)(http)
    }
    // val config = CORSConfig.default.withAllowedCredentials(false)

    // val cors = CORS(routes, config)
  }

  private val loggers: HttpApp[F] => HttpApp[F] = {
    { http: HttpApp[F] =>
      RequestLogger.httpApp(logHeaders = true, logBody = true)(http)
    } andThen { http: HttpApp[F] =>
      ResponseLogger.httpApp(logHeaders = true, logBody = true)(http)
    }
  }

  val httpApp: HttpApp[F] = loggers(middleware(healthCheckRoute <+> employeesRoute <+> companiesRoute).orNotFound)
}
