package xyz.funnycoding.http.routes

import cats._
import cats.syntax.all._
import org.http4s._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import xyz.funnycoding.algebras.Companies
import xyz.funnycoding.domain.companies._
import xyz.funnycoding.effects._
import xyz.funnycoding.http.decoder._
import xyz.funnycoding.http.json._

final class CompaniesRoute[F[_]: Defer: MonadThrow: JsonDecoder](companies: Companies[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/companies"

  private val httpRoutes = HttpRoutes.of[F] {
    case GET -> Root =>
      companies.findAll
        .flatMap(Ok(_))
        .recoverWith {
          case _: LoadCompaniesFailed =>
            InternalServerError()
        }

    case req @ POST -> Root =>
      req.decodeR[CompanyRequest] { companyRequest =>
        companies
          .insert(companyRequest)
          .flatMap(Created(_))
          .recoverWith {
            case InsertCompanyFailed(_) =>
              InternalServerError()
          }
      }

    case DELETE -> Root / UUIDVar(id) =>
      companies.delete(id).flatMap(Ok(_)).recoverWith {
        case DeleteCompanyFailed(_) =>
          InternalServerError()
      }
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
