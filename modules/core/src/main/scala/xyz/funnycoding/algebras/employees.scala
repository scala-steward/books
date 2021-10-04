package xyz.funnycoding.algebras

import cats.effect._
import cats.implicits._
import cats.MonadThrow
import com.sksamuel.elastic4s.http._
import com.sksamuel.elastic4s.circe._
import xyz.funnycoding.domain.employees._
import xyz.funnycoding.effects._
import xyz.funnycoding.http.json._
import io.chrisdavenport.log4cats.Logger

import java.util.UUID

trait Employees[F[_]] {
  def init(): F[Unit]
  def findAll(): F[List[Employee]]
  def insert(employeeRequest: EmployeeRequest): F[Unit]
  def findByCompanyId(companyId: UUID): F[List[Employee]]
}

final class LiveEmployees[F[_]: Sync: Logger: MonadThrow: GenUUID](els: ElasticClient)(
    implicit
    U: Functor[F],
    E: Executor[F]
) extends Employees[F] {

  import com.sksamuel.elastic4s.http.ElasticDsl._

  private val index  = "employees"
  private val `type` = "employee"

  override def init(): F[Unit] =
    els
      .execute(
        createIndex(index)
      )
      .flatMap {
        case RequestFailure(_, _, _, e) =>
          Logger[F].error(s"failed to init index $index cause: $e")
        case RequestSuccess(_, _, _, _) =>
          Logger[F].info(s"index $index created")
      }

  val searchAll = els.execute(search(index).query(matchAllQuery()))

  def searchByCompanyId(companyId: UUID) =
    els.execute(
      search(index)
        .query(matchQuery("companyId", companyId.toString))
    )

  // TODO implement me
  override def findAll(): F[List[Employee]] =
    searchAll.flatMap {
      case RequestFailure(_, _, _, e) =>
        Logger[F].error(s"failed to load employees cause: $e") *>
            MonadThrow[F].raiseError(LoadEmployeesFailed())
      case RequestSuccess(_, _, _, r) =>
        (for {
          res <- r.to[Employee].toList
        } yield res).traverse(_.pure[F])
    }

  override def findByCompanyId(companyId: UUID): F[List[Employee]] =
    searchByCompanyId(companyId).flatMap {
      case RequestFailure(_, _, _, e) =>
        Logger[F].error(s"failed to load employees by companyId ${companyId.toString} cause: $e") *>
            MonadThrow[F].raiseError(LoadEmployeesFailed())
      case RequestSuccess(_, _, _, r) =>
        (for {
          res <- r.to[Employee].toList
        } yield res).traverse(_.pure[F])
    }

  // TODO fix insert, employee must be stored in company
  override def insert(employeeRequest: EmployeeRequest): F[Unit] =
    GenUUID[F].make.flatMap { id =>
      els
        .execute(
          indexInto(index / `type`)
            .id(id.toString)
            .source(Employee.fromRequest(employeeRequest, id))
            .refreshImmediately
        )
        .flatMap {
          case RequestFailure(_, _, _, e) =>
            Logger[F].error(s"failed to insert employee with id ${id.toString}: $employeeRequest \n cause: $e") *>
                MonadThrow[F].raiseError(InsertEmployeeFailed(employeeRequest))
          case RequestSuccess(_, _, _, _) =>
            Logger[F].info(s"employee: $employeeRequest inserted")
        }
    }

}

object LiveEmployees {
  def make[F[_]: Sync: Logger: MonadThrow: GenUUID](
      els: ElasticClient
  )(implicit U: Functor[F], E: Executor[F]): F[Employees[F]] =
    Sync[F].delay {
      new LiveEmployees[F](els)
    }
}
