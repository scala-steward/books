package xyz.funnycoding.domain

import java.util.UUID

import eu.timepit.refined.types.string.NonEmptyString

import scala.util.control.NoStackTrace

object data {
  case class CreateBook(name: NonEmptyString)
  case class Book(uuid: UUID, name: NonEmptyString)

  case class BookNameInUse(name: NonEmptyString) extends NoStackTrace
}
