package xyz.funnycoding.domain

import java.util.UUID
import eu.timepit.refined.types.string.NonEmptyString
import xyz.funnycoding.domain.employees._

import scala.util.control.NoStackTrace

object companies {

  case class CreateBook(name: NonEmptyString)

  case class Book(uuid: UUID, name: NonEmptyString)

  case class BookNameInUse(name: NonEmptyString) extends NoStackTrace

  case class Company(
      id: UUID,
      name: String,
      address: String,
      language: String,
      // TODO [nh] fix me
      //                     mail: Email,
      email: String,
      vatNumber: String,
      phone: String,
      employees: List[Employee]
  )

  object Company {
    def fromRequest(companyRequest: CompanyRequest, id: UUID): Company =
      Company(
        id = id,
        name = companyRequest.name,
        address = companyRequest.address,
        language = companyRequest.language,
        email = companyRequest.email,
        vatNumber = companyRequest.vatNumber,
        phone = companyRequest.phone,
        employees = Nil
      )
  }

  case class CompanyRequest(
      name: String,
      address: String,
      language: String,
      // TODO [nh] fix me
      email: String,
      vatNumber: String,
      phone: String
  )

  case class InsertCompanyFailed(companyRequest: CompanyRequest) extends NoStackTrace
  case class DeleteCompanyFailed(id: UUID) extends NoStackTrace
  case class LoadCompaniesFailed() extends NoStackTrace

}
