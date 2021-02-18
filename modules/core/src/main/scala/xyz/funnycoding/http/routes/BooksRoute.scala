package xyz.funnycoding.http.routes

import cats._
import cats.implicits._
import org.http4s._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import xyz.funnycoding.algebras.Books
import xyz.funnycoding.http.json._
import xyz.funnycoding.domain.data._
import xyz.funnycoding.effects._
import xyz.funnycoding.http.decoder._

final class BooksRoute[F[_]: Defer: MonadThrow: JsonDecoder](books: Books[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/books"

  private val httpRoutes = HttpRoutes.of[F] {
    case GET -> Root =>
      Ok(
        books.findAll
      )
    case req @ POST -> Root =>
      req.decodeR[CreateBook] { createBook =>
        books.create(createBook).flatMap(Created(_)).recoverWith {
          case BookNameInUse(n) => Conflict(n.value)
        }
      }
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )
}
