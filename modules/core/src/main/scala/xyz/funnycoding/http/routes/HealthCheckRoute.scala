package xyz.funnycoding.http.routes

import cats._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import xyz.funnycoding.algebras.HealthCheck
import xyz.funnycoding.http.json._

final class HealthCheckRoute[F[_]: Defer: Monad](
    healthCheck: HealthCheck[F]
) extends Http4sDsl[F] {

  private[routes] val prefixPath = "/healthcheck"

  private val httpRoutes: HttpRoutes[F] = HttpRoutes.of[F] { case GET -> Root =>
    Ok(healthCheck.status)
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
