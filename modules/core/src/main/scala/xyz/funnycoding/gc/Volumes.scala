package xyz.funnycoding.gc

import org.http4s._
import cats.implicits._
import org.http4s.Method._
import org.http4s.circe._
import org.http4s.client._
import org.http4s.client.dsl.Http4sClientDsl
import xyz.funnycoding.domain.volume._
import xyz.funnycoding.effects._

trait Volumes[F[_]] {
  def get(volumeId: VolumeId): F[VolumeInfo]
}

final class LiveVolumes[F[_]: JsonDecoder: BracketThrow](client: Client[F]) extends Volumes[F] with Http4sClientDsl[F] {
  override def get(volumeId: VolumeId): F[VolumeInfo] =
    Uri
      .fromString(
        "" +
            s"https://www.googleapis.com/books/v1/volumes/${volumeId.value.value}"
      )
      .liftTo[F]
      .flatMap { uri =>
        GET(uri).flatMap { req =>
          client.run(req).use { r =>
            if (r.status == Status.Ok || r.status == Status.Conflict)
              r.asJsonDecode[VolumeInfo]
            else
              VolumeError(
                Option(r.status.reason).getOrElse("unknown")
              ).raiseError[F, VolumeInfo]

          }
        }
      }
}
