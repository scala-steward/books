package xyz.funnycoding.http.routes

import cats.effect._
import cats.implicits._
import org.http4s.Method._
import org.http4s.client.dsl.io._
import org.http4s.{ Status, Uri }
import suite.HttpTestSuite
import xyz.funnycoding.algebras.Books
import xyz.funnycoding.http.json._
import xyz.funnycoding.domain.data._
import xyz.funnycoding.arbitrairies._

class BooksRouteSpec extends HttpTestSuite {
  forAll { _: List[Book] =>
    spec("GET books [OK]") {
      GET(Uri.uri("/books")).flatMap { req =>
        val routes = new BooksRoute[IO](new TestBooks).routes
        assertHttpStatus(routes, req)(Status.Ok)
      }
    }
  }
  forAll { createBook: CreateBook =>
    spec("POST book [OK]") {
      POST(createBook, Uri.uri("/books")).flatMap { req =>
        val routes = new BooksRoute[IO](new TestBooks).routes
        assertHttpStatus(routes, req)(Status.Ok)
      }

    }

  }
}

protected class TestBooks extends Books[IO] {
  override def create(createBook: CreateBook): IO[Unit] = IO.unit

  override def findAll: IO[List[Book]] = List.empty[Book].pure[IO]
}
