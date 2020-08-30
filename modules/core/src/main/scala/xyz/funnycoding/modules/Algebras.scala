package xyz.funnycoding.modules

import cats.effect.{ Resource, Sync }
import cats.implicits._
import skunk.Session
import xyz.funnycoding.algebras._

object Algebras {
  def make[F[_]: Sync](sessionPool: Resource[F, Session[F]]): F[Algebras[F]] =
    for {
      books <- LiveBooks.make[F](sessionPool)
    } yield new Algebras(books)
}

final class Algebras[F[_]: Sync] private (
    val books: Books[F]
)
