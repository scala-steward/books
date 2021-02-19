package xyz.funnycoding.algebras

import cats.effect._
import cats.implicits._
import com.sksamuel.elastic4s.http._
import com.sksamuel.elastic4s.circe._
import xyz.funnycoding.domain.data._
import xyz.funnycoding.effects._
import xyz.funnycoding.http.json._
import io.chrisdavenport.log4cats.Logger

import java.util.UUID

trait Companies[F[_]] {
  def findAll: F[List[Company]]
  def insert(companyRequest: CompanyRequest): F[Unit]
  def delete(id: UUID): F[Unit]
}

final class LiveCompanies[F[_]: Sync: Logger: MonadThrow: GenUUID](els: ElasticClient)(implicit U: Functor[F], E: Executor[F])
    extends Companies[F] {
  import com.sksamuel.elastic4s.http.ElasticDsl._

  override def findAll: F[List[Company]] = {
    searchCompanies.flatMap {
      case RequestFailure(_, _, _, e) =>
        Logger[F].error(s"failed to load companies cause: $e") *>
          MonadThrow[F].raiseError(LoadCompaniesFailed())
      case RequestSuccess(_, _, _, r) =>
        (for {
          res <- r.to[Company].toList
        } yield res).traverse(_.pure[F])
    }

  }

  val searchCompanies = els.execute(search("companies").query(matchAllQuery()))

  override def insert(companyRequest: CompanyRequest): F[Unit] = {
    GenUUID[F].make.flatMap { id =>
      els
        .execute(
          indexInto("companies" / "company").id(id.toString).source(Company.fromRequest(companyRequest, id)).refreshImmediately
        )
        .flatMap {
          case RequestFailure(_, _, _, e) =>
            Logger[F].error(s"failed to insert company with id ${id.toString}: $companyRequest \n cause: $e") *>
              MonadThrow[F].raiseError(InsertCompanyFailed(companyRequest))
          case RequestSuccess(_, _, _, _) =>
            Logger[F].info(s"company: $companyRequest inserted")
        }
    }
  }

  override def delete(id: UUID): F[Unit] = els.execute(
    deleteById("companies","company", id.toString)
  ).flatMap {
    case RequestFailure(_, _, _, e) =>
      Logger[F].error(s"failed to delete company with id ${id.toString} \n cause: $e") *>
        MonadThrow[F].raiseError(DeleteCompanyFailed(id))
    case RequestSuccess(_, _, _, _) =>
      Logger[F].info(s"company: ${id.toString} deleted")
  }
}

object LiveCompanies {
  def make[F[_]: Sync: Logger: MonadThrow](
      els: ElasticClient)(implicit U: Functor[F], E: Executor[F]): F[Companies[F]] =
    Sync[F].delay {
      new LiveCompanies[F](els)
    }
}
