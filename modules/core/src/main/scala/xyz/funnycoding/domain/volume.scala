package xyz.funnycoding.domain

import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype
import scala.util.control.NoStackTrace

object volume {
  @newtype case class VolumeId(value: NonEmptyString)

  @newtype case class VolumeIdparam(value: NonEmptyString)

  @newtype case class Title(value: NonEmptyString)
  @newtype case class Subtitle(value: NonEmptyString)
  @newtype case class Author(value: NonEmptyString)
  case class VolumeInfo(title: Title, subtitle: Subtitle, authors: List[Author])
  case class Volume(id: VolumeId, volumeInfo: VolumeInfo)

  object VolumeIdParamMatcher {
    def unapply(arg: String): Option[NonEmptyString] =
      NonEmptyString.from(arg).toOption
  }

  case class VolumeError(cause: String) extends NoStackTrace

}
