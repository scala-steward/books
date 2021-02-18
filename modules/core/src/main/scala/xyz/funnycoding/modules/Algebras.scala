package xyz.funnycoding.modules

import cats.Parallel
import cats.effect.{Concurrent, Resource, Sync, Timer}
import cats.implicits._
import com.sksamuel.elastic4s.http.{ElasticClient, Executor, Functor}
import io.chrisdavenport.log4cats.Logger
import org.http4s.client.Client
import skunk.Session
import xyz.funnycoding.algebras._

object Algebras {
  def make[F[_]: Concurrent: Parallel: Timer: Logger](
      sessionPool: Resource[F, Session[F]],
      client: Client[F],
      els: ElasticClient)(implicit U: Functor[F], E: Executor[F]): F[Algebras[F]] =
    for {
      volumes <- LiveVolumes.make(client)
      books <- LiveBooks.make[F](sessionPool)
      companies <- LiveCompanies.make[F](els)
      employees <- LiveEmployees.make[F]()
      healthcheck <- LiveHealthCheck.make[F](sessionPool, els)
    } yield new Algebras(volumes, books, companies, employees, healthcheck)
}

final class Algebras[F[_]: Sync] private (
    val volumes: Volumes[F],
    val books: Books[F],
    val companies: Companies[F],
    val employees: Employees[F],
    val healthcheck: HealthCheck[F]
)
