package xyz.funnycoding.config

import cats.Applicative
import xyz.funnycoding.config.data._
import eu.timepit.refined.auto._

import scala.concurrent.duration._

object load {
  def apply[F[_]: Applicative]: F[AppConfig] =
    Applicative[F].pure(
      AppConfig(
        PostgreSQLConfig(
          host = "localhost",
          port = 5432,
          user = "postgres",
          database = "booksdb",
          max = 10
        ),
        HttpClientConfig(
          connectTimeout = 2.seconds,
          requestTimeout = 2.seconds
        )
      )
    )
}
