package xyz.funnycoding

import cats.effect._
import io.chrisdavenport.log4cats.Logger
import skunk._
import xyz.funnycoding.config.data._
import natchez.Trace.Implicits.noop // needed for skunk

final case class AppResources[F[_]](psql: Resource[F, Session[F]]) {}

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
    mkPostgreSqlResource(appConfig.postgreSQL).map(AppResources.apply[F])
  }
}
