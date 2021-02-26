package xyz.funnycoding.http

import cats.Applicative
import io.circe._
import io.circe.generic.semiauto._
import io.circe.refined._
import io.estatico.newtype._
import io.estatico.newtype.ops._
import org.http4s.EntityEncoder
import org.http4s.circe.jsonEncoderOf
import xyz.funnycoding.domain.companies._
import xyz.funnycoding.domain.employees
import xyz.funnycoding.domain.healthcheck.AppStatus
import xyz.funnycoding.domain.employees._
import xyz.funnycoding.domain.volume._

object json extends JsonCodecs {
  implicit def deriveEntityEncoder[F[_]: Applicative, A: Encoder]: EntityEncoder[F, A] = jsonEncoderOf[F, A]
}
private[http] trait JsonCodecs {
  implicit val createBookDecoder: Decoder[CreateBook] = deriveDecoder[CreateBook]
  implicit val createBookEncoder: Encoder[CreateBook] = deriveEncoder[CreateBook]

  implicit val bookDecoder: Decoder[Book] = deriveDecoder[Book]
  implicit val bookEncoder: Encoder[Book] = deriveEncoder[Book]

  implicit val volumeInfoDecoder: Decoder[VolumeInfo] = deriveDecoder[VolumeInfo]

  implicit val volumeInfoEncoder: Encoder[VolumeInfo] = deriveEncoder[VolumeInfo].mapJsonObject(
    _.filter {
      case ("subtitle", value) =>
        !value.isNull
      case ("description", value) =>
        !value.isNull
      case ("categories", value) =>
        !value.isNull
      case ("publisher", value) =>
        !value.isNull
      case _ =>
        true
    }
  )

  implicit val volumeDecoder: Decoder[Volume] = deriveDecoder[Volume]

  implicit val volumeEncoder: Encoder[Volume] = deriveEncoder[Volume]

  implicit val volumesDecoder2: Decoder[Volumes] = deriveDecoder[Volumes]

  implicit val editorialLineDecoder: Decoder[EditorialLine] = Decoder.decodeString.emap {str =>
    str.toUpperCase match {
      case "FICTION" => Right(Fiction)
      case "YOUTH" => Right(Youth)
      case _ => Left(str)
    }
  }

  implicit val editorialLineEncoder: Encoder[EditorialLine] = Encoder.encodeString.contramap[EditorialLine] {
    case employees.Fiction => "FICTION"
    case employees.Youth => "YOUTH"
  }

  implicit val companyEmployeeEncoder: Encoder[Employee] = deriveEncoder[Employee]
  implicit val companyEmployeeDecoder: Decoder[Employee] = deriveDecoder[Employee]

  implicit val employeeRequestEncoder: Encoder[EmployeeRequest] = deriveEncoder[EmployeeRequest]
  implicit val employeeRequestDecoder: Decoder[EmployeeRequest] = deriveDecoder[EmployeeRequest]

  implicit val companyEncoder: Encoder[Company] = deriveEncoder[Company]
  implicit val companyDecoder: Decoder[Company] = deriveDecoder[Company]

  implicit val companyRequestEncoder: Encoder[CompanyRequest] = deriveEncoder[CompanyRequest]
  implicit val companyRequestDecoder: Decoder[CompanyRequest] = deriveDecoder[CompanyRequest]

  implicit val appStatusEncoder: Encoder[AppStatus] = deriveEncoder[AppStatus]
  implicit val appStatusDecoder: Decoder[AppStatus] = deriveDecoder[AppStatus]

  // ----- Coercible codecs -----
  implicit def coercibleDecoder[A: Coercible[B, *], B: Decoder]: Decoder[A] = Decoder[B].map(_.coerce[A])

  implicit def coercibleEncoder[A: Coercible[B, *], B: Encoder]: Encoder[A] =
    Encoder[B].contramap(_.repr.asInstanceOf[B])

  implicit def coercibleKeyDecoder[A: Coercible[B, *], B: KeyDecoder]: KeyDecoder[A] = KeyDecoder[B].map(_.coerce[A])

  implicit def coercibleKeyEncoder[A: Coercible[B, *], B: KeyEncoder]: KeyEncoder[A] =
    KeyEncoder[B].contramap[A](_.repr.asInstanceOf[B])

}
