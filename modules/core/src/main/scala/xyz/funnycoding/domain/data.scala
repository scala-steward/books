package xyz.funnycoding.domain

import java.util.UUID
import eu.timepit.refined.types.string.NonEmptyString
import xyz.funnycoding.domain.refinements.Email

import scala.util.control.NoStackTrace

object data {

  sealed trait EditorialLine

  case class CreateBook(name: NonEmptyString)

  case class Book(uuid: UUID, name: NonEmptyString)

  case class BookNameInUse(name: NonEmptyString) extends NoStackTrace

  case class Employee(company: String,
                      firstName: String,
                      lastName: String,
                      address: String,
                      email: Email,
                      phone: String,
                      editorialLine: EditorialLine)

  case class Company(name: String,
                     adresse: String,
                     language: String,
                     // TODO fix me
//                     mail: Email,
                     mail: String,
                     vatNumber: String,
                     phone: String,
                     employees: List[Employee])


  case object Fiction extends EditorialLine
  case object Youth extends EditorialLine

}
