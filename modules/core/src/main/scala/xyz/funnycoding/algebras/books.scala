package xyz.funnycoding.algebras

import java.util.UUID

import cats.effect._
import cats.implicits._
import eu.timepit.refined.types.string.NonEmptyString
import skunk._
import skunk.codec.all._
import skunk.implicits._
import xyz.funnycoding.domain.data._
import xyz.funnycoding.effects._

trait Books[F[_]] {
  def create(createBook: CreateBook): F[UUID]
  def findAll: F[List[Book]]
}

final class LiveBooks[F[_]: BracketThrow: GenUUID](sessionPool: Resource[F, Session[F]]) extends Books[F] {
  import BooksQueries._
  override def create(createBook: CreateBook): F[UUID] =
    sessionPool.use { session =>
      session.prepare(insertBook).use { cmd =>
        GenUUID[F].make.flatMap { id =>
          cmd
            .execute(Book(id, createBook.name))
            .as(id)
            .handleErrorWith {
              case SqlState.UniqueViolation(_) =>
                BookNameInUse(createBook.name).raiseError[F, UUID]
            }

        }
      }
    }
  override def findAll: F[List[Book]] =
    sessionPool.use(_.execute(selectAll))
}

private object BooksQueries {
  val codec: Codec[Book] =
    (uuid ~ varchar).imap {
      // TODO FIX ME
      case i ~ n => Book(i, NonEmptyString.unsafeFrom(n))
    }(c => c.uuid ~ c.name.value)

  val insertBook: Command[Book] =
    sql"INSERT INTO books VALUES ($codec)".command

  val selectAll: Query[Void, Book] =
    sql"""
        SELECT * FROM books
       """.query(codec)
}

object LiveBooks {
  def make[F[_]: Sync](sessionPool: Resource[F, Session[F]]): F[LiveBooks[F]] = Sync[F].delay {
    new LiveBooks[F](sessionPool)
  }
}
