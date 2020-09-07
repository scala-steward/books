package xyz.funnycoding.domain

import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype
import scala.util.control.NoStackTrace

object volume {
  @newtype case class VolumeId(value: NonEmptyString)

  @newtype case class Description(value: NonEmptyString)
  @newtype case class Category(value: NonEmptyString)
  @newtype case class Title(value: NonEmptyString)
  @newtype case class Subtitle(value: NonEmptyString)
  @newtype case class Author(value: NonEmptyString)
  @newtype case class Publisher(value: NonEmptyString)

  case class VolumeInfo(
      title: Title,
      subtitle: Subtitle,
      authors: List[Author],
      description: Description,
      categories: List[Category]
  )

  case class Volume(id: VolumeId, volumeInfo: VolumeInfo)

  case class VolumeError(cause: String) extends NoStackTrace

}
