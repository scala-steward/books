package xyz.funnycoding.http.routes

import cats._
import cats.implicits._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl._
import org.http4s.server.Router
import xyz.funnycoding.http.json._
import xyz.funnycoding.domain.volume._
import xyz.funnycoding.effects._
import eu.timepit.refined.auto._
import xyz.funnycoding.algebras.Volumes

final class VolumesRoute[F[_]: Defer: MonadThrow: JsonDecoder](volumes: Volumes[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/volumes"

  private val httpRoutes = HttpRoutes.of[F] {
    case GET -> Root / NonEmptyStringPathVar(id) =>
      volumes.get(VolumeId(id)).flatMap {
        case Some(v) => Ok(v)
        case None    => NotFound(s"volumeId ${id.value} not found")
      }
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )
}
