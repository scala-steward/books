package xyz.funnycoding.domain

import java.util.UUID
import scala.util.control.NoStackTrace

object employees {
  sealed trait EditorialLine
  case object Fiction extends EditorialLine
  case object Youth extends EditorialLine

  case class Employee(
      id: UUID,
      companyId: UUID,
      firstName: String,
      lastName: String,
      address: String,
      email: String,
      phone: String,
      editorialLine: EditorialLine
  )

  object Employee {
    def fromRequest(employeeRequest: EmployeeRequest, id: UUID): Employee =
      Employee(
        id = id,
        companyId = employeeRequest.companyId,
        firstName = employeeRequest.firstName,
        lastName = employeeRequest.lastName,
        address = employeeRequest.address,
        email = employeeRequest.email,
        phone = employeeRequest.phone,
        editorialLine = employeeRequest.editorialLine
      )
  }

  case class EmployeeRequest(
      companyId: UUID,
      firstName: String,
      lastName: String,
      address: String,
      email: String,
      phone: String,
      editorialLine: EditorialLine
  )

  case class InsertEmployeeFailed(employeeRequest: EmployeeRequest) extends NoStackTrace
  case class DeleteEmployeeFailed(id: UUID) extends NoStackTrace
  case class LoadEmployeesFailed() extends NoStackTrace

}
