package xyz.funnycoding.domain

import cats.data.NonEmptyList
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
      subtitle: Option[Subtitle],
      authors: List[Author],
      description: Option[Description],
      categories: Option[List[Category]],
      publisher: Option[Publisher]
  )

  case class Volume(id: VolumeId, volumeInfo: VolumeInfo)

  case class Volumes(totalItems: Int, items: List[Volume])

  type VolumesSearch = NonEmptyList[VolumeSearch]

  object VolumesSearch {
    def query(volumesSearch: VolumesSearch): String =
      volumesSearch
        .foldLeft("") {
          case (acc, el) => s"$acc+${el.query}"
        }
        .tail
  }

  sealed trait VolumeSearch {
    def query: String
  }
  case class Intitle(value: NonEmptyString) extends VolumeSearch {
    override def query: String =
      s"intitle:${value.value}"
  }
  case class InAuthor(value: NonEmptyString) extends VolumeSearch {
    override def query: String =
      s"inauthor:${value.value}"
  }
  case class InPublisher(value: NonEmptyString) extends VolumeSearch {
    override def query: String =
      s"inpublisher:${value.value}"
  }
  case class InCategory(value: NonEmptyString) extends VolumeSearch {
    override def query: String =
      s"subject:${value.value}"
  }

  case class VolumeError(cause: String) extends NoStackTrace

}
