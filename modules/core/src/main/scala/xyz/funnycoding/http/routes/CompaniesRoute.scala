package xyz.funnycoding.http.routes

import cats._
import org.http4s._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import xyz.funnycoding.algebras.Companies
import xyz.funnycoding.effects._
import xyz.funnycoding.http.json._

final class CompaniesRoute[F[_]: Defer: MonadThrow: JsonDecoder](companies: Companies[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/companies"

  private val httpRoutes = HttpRoutes.of[F] {
    case GET -> Root =>
      Ok(
        companies.findAll
      )
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )

}
