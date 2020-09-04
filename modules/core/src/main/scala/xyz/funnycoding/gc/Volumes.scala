package xyz.funnycoding.gc

import cats.effect.Sync
import org.http4s._
import cats.implicits._
import org.http4s.Method._
import org.http4s.circe._
import org.http4s.client._
import org.http4s.client.dsl.Http4sClientDsl
import xyz.funnycoding.domain.volume._
import xyz.funnycoding.effects._
import xyz.funnycoding.http.json._

trait Volumes[F[_]] {
  def get(volumeId: VolumeId): F[Volume]
}

object LiveVolumes {
  def make[F[_]: Sync](client: Client[F]): F[Volumes[F]] = Sync[F].delay(
    new LiveVolumes[F](client)
  )
}

final class LiveVolumes[F[_]: JsonDecoder: BracketThrow](client: Client[F]) extends Volumes[F] with Http4sClientDsl[F] {
  override def get(volumeId: VolumeId): F[Volume] = {
    val value = s"https://www.googleapis.com/books/v1/volumes/${volumeId.value.value}"
    Uri
      .fromString(
        value
      )
      .liftTo[F]
      .flatMap { uri =>
        GET(uri).flatMap { req =>
          client.run(req).use { r =>
            if (r.status == Status.Ok) {
              r.asJsonDecode[Volume]
            } else
              VolumeError(
                Option(r.status.reason).getOrElse("unknown")
              ).raiseError[F, Volume]

          }
        }
      }
  }
}
