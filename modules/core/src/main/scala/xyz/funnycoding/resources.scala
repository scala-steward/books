package xyz.funnycoding

import cats.effect._
import cats.implicits._
import io.chrisdavenport.log4cats.Logger
import skunk._
import xyz.funnycoding.config.data._
import natchez.Trace.Implicits.noop
import org.http4s.client._
import org.http4s.client.blaze.BlazeClientBuilder

import scala.concurrent.ExecutionContext

final case class AppResources[F[_]](psql: Resource[F, Session[F]], client: Client[F]) {}

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

    (mkPostgreSqlResource(appConfig.postgreSQL), mkHttpClient(appConfig.httpClient)).mapN(AppResources.apply[F])
  }
}
