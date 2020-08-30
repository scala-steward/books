package xyz.funnycoding.http

import cats.Applicative
import io.circe._
import io.circe.generic.semiauto._
import io.circe.refined._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import xyz.funnycoding.domain.data._

object json extends JsonCodecs {
  implicit def deriveEntityEncoder[F[_]: Applicative, A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]
}
private[http] trait JsonCodecs {
  implicit val createBookDecoder = deriveDecoder[CreateBook]
  implicit val createBookEncoder = deriveEncoder[CreateBook]

  implicit val bookDecoder = deriveDecoder[Book]
  implicit val bookEncoder = deriveEncoder[Book]

}
