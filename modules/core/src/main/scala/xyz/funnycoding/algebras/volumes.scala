package xyz.funnycoding.algebras

import cats.effect.Sync
import cats.implicits._
import org.http4s.Method._
import org.http4s._
import org.http4s.circe._
import org.http4s.client._
import org.http4s.client.dsl.Http4sClientDsl
import xyz.funnycoding.domain.volume
import xyz.funnycoding.domain.volume._
import xyz.funnycoding.effects._
import xyz.funnycoding.http.json._

trait Volumes[F[_]] {
  def get(volumeId: VolumeId): F[Option[Volume]]
  def search(volumesSearch: VolumesSearch): F[List[Volume]]
}

object LiveVolumes {
  def make[F[_]: Sync](client: Client[F]): F[Volumes[F]] = Sync[F].delay(
    new LiveVolumes[F](client)
  )
}

final class LiveVolumes[F[_]: JsonDecoder: BracketThrow](client: Client[F]) extends Volumes[F] with Http4sClientDsl[F] {
  override def get(volumeId: VolumeId): F[Option[Volume]] =
    Uri
      .fromString(
        s"https://www.googleapis.com/books/v1/volumes/${volumeId.value.value}"
      )
      .liftTo[F]
      .flatMap { uri =>
        GET(uri).flatMap { req =>
          client.run(req).use { r =>
            if (r.status == Status.Ok) {
              r.asJsonDecode[Volume].map(_.some)
            } else if (r.status == Status.ServiceUnavailable) {
              none[Volume].pure[F]
            } else
              VolumeError(
                Option(r.status.reason).getOrElse("unknown")
              ).raiseError[F, Option[Volume]]

          }
        }
      }

  override def search(volumesSearch: VolumesSearch): F[List[Volume]] =
    Uri
      .fromString(
        s"https://www.googleapis.com/books/v1/volumes?q=${VolumesSearch.query(volumesSearch)}"
      )
      .liftTo[F]
      .flatMap { uri =>
        println(uri)
        GET(uri).flatMap { req =>
          client.run(req).use { r =>
            if (r.status == Status.Ok) {
              r.asJsonDecode[volume.Volumes].map(_.items)
            } else
              VolumeError(
                Option(r.status.reason).getOrElse("unknown")
              ).raiseError[F, List[Volume]]

          }
        }
      }

}
