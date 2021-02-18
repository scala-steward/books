package xyz.funnycoding.algebras

import scala.concurrent.duration._

import xyz.funnycoding.domain.healthcheck._

import cats.Parallel
import cats.effect._
import cats.effect.implicits._
import cats.syntax.all._
import com.sksamuel.elastic4s.http.ElasticClient
import skunk._
import skunk.codec.all._
import skunk.implicits._

trait HealthCheck[F[_]] {
  def status: F[AppStatus]
}

object LiveHealthCheck {
  def make[F[_]: Concurrent: Parallel: Timer](
      sessionPool: Resource[F, Session[F]],
      els: ElasticClient
  ): F[HealthCheck[F]] =
    Sync[F].delay(
      new LiveHealthCheck[F](sessionPool, els)
    )
}

final class LiveHealthCheck[F[_]: Concurrent: Parallel: Timer] private (
    sessionPool: Resource[F, Session[F]],
    els: ElasticClient
) extends HealthCheck[F] {

  val q: Query[Void, Int] = sql"SELECT pid FROM pg_stat_activity".query(int4)

  val postgresHealth: F[PostgresStatus] = sessionPool
    .use(_.execute(q))
    .map(_.nonEmpty)
    .timeout(1.second)
    .orElse(false.pure[F])
    .map(PostgresStatus.apply)

  // TODO [nh] implement me
  val elsHealth: F[ElasticSearchStatus] = {
    println(els)
    ElasticSearchStatus(true).pure[F]
  }

  override def status: F[AppStatus] = (elsHealth, postgresHealth).parMapN(AppStatus)
}
