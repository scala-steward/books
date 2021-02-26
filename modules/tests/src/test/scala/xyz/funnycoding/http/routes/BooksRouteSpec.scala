package xyz.funnycoding.http.routes

import java.util.UUID

import cats.effect._
import cats.implicits._
import org.http4s.Method._
import org.http4s.client.dsl.io._
import org.http4s.{ Status, Uri }
import suite.HttpTestSuite
import xyz.funnycoding.algebras.Books
import xyz.funnycoding.http.json._
import xyz.funnycoding.domain.companies._
import xyz.funnycoding.arbitrairies._

class BooksRouteSpec extends HttpTestSuite {

  def dataBooks(books: List[Book]) = new TestBooks {
    override def findAll: IO[List[Book]] = books.pure[IO]
  }

  forAll { books: List[Book] =>
    spec("GET books [OK]") {
      GET(Uri.uri("/books")).flatMap { req =>
        val routes = new BooksRoute[IO](dataBooks(books)).routes
        assertHttp(routes, req)(Status.Ok, books)
      }
    }
  }
  forAll { createBook: CreateBook =>
    spec("POST book [OK]") {
      POST(createBook, Uri.uri("/books")).flatMap { req =>
        val routes = new BooksRoute[IO](new TestBooks).routes
        assertHttpStatus(routes, req)(Status.Created)
      }

    }

  }
}

protected class TestBooks extends Books[IO] {
  override def create(createBook: CreateBook): IO[UUID] = IO {
    UUID.randomUUID()
  }

  override def findAll: IO[List[Book]] = List.empty[Book].pure[IO]
}
