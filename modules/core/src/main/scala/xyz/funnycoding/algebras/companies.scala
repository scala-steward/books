package xyz.funnycoding.algebras

import cats.effect._
import cats.implicits._
import com.sksamuel.elastic4s.http._
import xyz.funnycoding.domain.data.Company
import com.sksamuel.elastic4s.circe._
//import com.sksamuel.elastic4s.http.index.CreateIndexResponse
import io.chrisdavenport.log4cats.Logger
import xyz.funnycoding.effects.MonadThrow
import xyz.funnycoding.http.json._

trait Companies[F[_]] {
  def findAll: F[List[Company]]
  def insert(company: Company): F[Unit]
}

final class LiveCompanies[F[_]: Sync: Logger: MonadThrow](els: ElasticClient)(implicit U: Functor[F], E: Executor[F])
    extends Companies[F] {
  import com.sksamuel.elastic4s.http.ElasticDsl._

  // TODO implement me
  override def findAll: F[List[Company]] = {
    searchCompanies.flatMap {
      case RequestFailure(_, _, _, e) =>
        Logger[F].error(s"failed to load companies cause: $e") *> Sync[F].point(Nil)
      case RequestSuccess(_, _, _, r) =>
        val value =
          for {
            res <- r.to[Company].toList
          } yield res
        value.traverse(_.pure[F])
    }

  }

  val searchCompanies = els.execute(search("companies").query(matchAllQuery()))

  override def insert(company: Company): F[Unit] =
    els
      .execute(
        indexInto("companies" / "company").source(company).refreshImmediately
      )
      .flatMap {
        case RequestFailure(_, _, _, e) =>
          Logger[F].error(s"failed to insert company: $company \n cause: $e")
        case RequestSuccess(_, _, _, _) =>
          Logger[F].info(s"company: $company inserted")
      }
}

object LiveCompanies {
  def make[F[_]: Sync: Logger: MonadThrow](
      els: ElasticClient)(implicit U: Functor[F], E: Executor[F]): F[Companies[F]] =
    Sync[F].delay {
      new LiveCompanies[F](els)
    }
}
