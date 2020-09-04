package xyz.funnycoding.http

import cats.Applicative
import io.circe._
import io.circe.generic.semiauto._
import io.circe.refined._
import io.estatico.newtype._
import io.estatico.newtype.ops._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import xyz.funnycoding.domain.data._
import xyz.funnycoding.domain.volume._

object json extends JsonCodecs {
  implicit def deriveEntityEncoder[F[_]: Applicative, A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]
}
private[http] trait JsonCodecs {
  implicit val createBookDecoder = deriveDecoder[CreateBook]
  implicit val createBookEncoder = deriveEncoder[CreateBook]

  implicit val bookDecoder = deriveDecoder[Book]
  implicit val bookEncoder = deriveEncoder[Book]

  implicit val volumeInfoDecoder =
    deriveDecoder[VolumeInfo]

  implicit val volumeInfoEncoder =
    deriveEncoder[VolumeInfo]

  implicit val volumeDecoder =
    deriveDecoder[Volume]

  implicit val volumeEncoder =
    deriveEncoder[Volume]

  // ----- Coercible codecs -----
  implicit def coercibleDecoder[A: Coercible[B, *], B: Decoder]: Decoder[A] =
    Decoder[B].map(_.coerce[A])

  implicit def coercibleEncoder[A: Coercible[B, *], B: Encoder]: Encoder[A] =
    Encoder[B].contramap(_.repr.asInstanceOf[B])

  implicit def coercibleKeyDecoder[A: Coercible[B, *], B: KeyDecoder]: KeyDecoder[A] =
    KeyDecoder[B].map(_.coerce[A])

  implicit def coercibleKeyEncoder[A: Coercible[B, *], B: KeyEncoder]: KeyEncoder[A] =
    KeyEncoder[B].contramap[A](_.repr.asInstanceOf[B])

}
