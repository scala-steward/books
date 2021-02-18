package xyz.funnycoding.config

import eu.timepit.refined.types.net.UserPortNumber
import eu.timepit.refined.types.numeric.PosInt
import eu.timepit.refined.types.string.NonEmptyString

import scala.concurrent.duration.FiniteDuration

object data {
  case class AppConfig(postgreSQL: PostgreSQLConfig, httpClient: HttpClientConfig, els: ElsConfig)

  case class PostgreSQLConfig(
      host: NonEmptyString,
      port: UserPortNumber,
      user: NonEmptyString,
      database: NonEmptyString,
      max: PosInt
  )

  case class HttpClientConfig(
      connectTimeout: FiniteDuration,
      requestTimeout: FiniteDuration
  )

  case class ElsConfig(
      host: String,
      port: UserPortNumber
  )
}
