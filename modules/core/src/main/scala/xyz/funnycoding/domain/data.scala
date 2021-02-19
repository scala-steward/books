package xyz.funnycoding.domain

import java.util.UUID
import eu.timepit.refined.types.string.NonEmptyString
import xyz.funnycoding.domain.refinements.Email

import scala.util.control.NoStackTrace

object data {

  sealed trait EditorialLine
  case object Fiction extends EditorialLine
  case object Youth extends EditorialLine

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

  case class Company(id: UUID,
                     name: String,
                     adresse: String,
                     language: String,
                     // TODO [nh] fix me
//                     mail: Email,
                     mail: String,
                     vatNumber: String,
                     phone: String,
                     employees: List[Employee])

  object Company {
    def fromRequest(companyRequest: CompanyRequest, id: UUID): Company =
      Company(
        id = id,
        name = companyRequest.name,
        adresse = companyRequest.adresse,
        language = companyRequest.language,
        mail = companyRequest.mail,
        vatNumber = companyRequest.vatNumber,
        phone = companyRequest.phone,
        employees = Nil
      )
  }

  case class CompanyRequest(name: String,
                            adresse: String,
                            language: String,
                            // TODO [nh] fix me
                            mail: String,
                            vatNumber: String,
                            phone: String)

  case class InsertCompanyFailed(companyRequest: CompanyRequest) extends NoStackTrace
  case class DeleteCompanyFailed(id: UUID) extends NoStackTrace
  case class LoadCompaniesFailed() extends NoStackTrace

}
