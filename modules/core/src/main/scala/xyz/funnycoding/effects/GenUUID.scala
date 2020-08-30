package xyz.funnycoding.effects

import java.util.UUID

import cats.effect.Sync

trait GenUUID[F[_]] {
  def make: F[UUID]
}
object GenUUID {
  def apply[F[_]](implicit ev: GenUUID[F]): GenUUID[F] = ev
  implicit def syncGenUUID[F[_]: Sync]: GenUUID[F] =
    new GenUUID[F] {
      def make: F[UUID] =
        Sync[F].delay(UUID.randomUUID())
    }
}
