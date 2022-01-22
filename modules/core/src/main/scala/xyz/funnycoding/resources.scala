package xyz.funnycoding

import cats.effect._
import cats.implicits._
import com.sksamuel.elastic4s.http.{ ElasticClient, ElasticProperties }
import org.typelevel.log4cats.Logger
import skunk._
import xyz.funnycoding.config.data._
import natchez.Trace.Implicits.noop
import org.http4s.client._
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext

final case class AppResources[F[_]](psql: Resource[F, Session[F]], client: Client[F], els: Resource[F, ElasticClient])

object AppResources {
  def make[F[_]: ConcurrentEffect: ContextShift: Logger](appConfig: AppConfig): Resource[F, AppResources[F]] = {
    def mkPostgreSqlResource(c: PostgreSQLConfig): SessionPool[F] =
      Session
        .pooled[F](
          host = c.host.value,
          port = c.port.value,
          user = c.user.value,
          database = c.database.value,
          max = c.max.value
        )
    def mkHttpClient(c: HttpClientConfig): Resource[F, Client[F]] =
      BlazeClientBuilder[F](ExecutionContext.global)
        .withConnectTimeout(c.connectTimeout)
        .withRequestTimeout(c.requestTimeout)
        .resource

    def mkElsClient(c: ElsConfig) = Resource.pure[F, Resource[F, ElasticClient]] {
      Resource.make {
        ConcurrentEffect[F].delay {
          ElasticClient(
            ElasticProperties(
              s"${c.host}:${c.port.value}"
            )
          )
        }
      }(
        c =>
          ConcurrentEffect[F].delay {
            c.close()
          }
      )
    }

    (mkPostgreSqlResource(appConfig.postgreSQL), mkHttpClient(appConfig.httpClient), mkElsClient(appConfig.els))
      .mapN(AppResources.apply[F])
  }
}
