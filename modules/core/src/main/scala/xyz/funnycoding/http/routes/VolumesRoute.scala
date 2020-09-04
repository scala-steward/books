package xyz.funnycoding.http.routes

import cats._
import org.http4s._
import org.http4s.circe.JsonDecoder
import org.http4s.dsl.Http4sDsl
import org.http4s.server.Router
import xyz.funnycoding.http.json._
import xyz.funnycoding.domain.volume._
import xyz.funnycoding.effects._
import xyz.funnycoding.gc._
import eu.timepit.refined.auto._

final class VolumesRoute[F[_]: Defer: MonadThrow: JsonDecoder](volumes: Volumes[F]) extends Http4sDsl[F] {
  private[routes] val prefixPath = "/volumes"

  private val httpRoutes = HttpRoutes.of[F] {
    case GET -> Root =>
      Ok(
        volumes.get(VolumeId("TZqxDwAAQBAJ"))
      )
  }

  val routes: HttpRoutes[F] = Router(
    prefixPath -> httpRoutes
  )
}
