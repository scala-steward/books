package xyz.funnycoding.modules

import cats.effect.{ Resource, Sync }
import cats.implicits._
import org.http4s.client.Client
import skunk.Session
import xyz.funnycoding.algebras._
import xyz.funnycoding.gc._

object Algebras {
  def make[F[_]: Sync](sessionPool: Resource[F, Session[F]], client: Client[F]): F[Algebras[F]] =
    for {
      volumes <- LiveVolumes.make(client)
      books <- LiveBooks.make[F](sessionPool)
    } yield new Algebras(volumes, books)
}

final class Algebras[F[_]: Sync] private (
    val volumes: Volumes[F],
    val books: Books[F]
)
