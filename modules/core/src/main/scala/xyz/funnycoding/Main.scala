package xyz.funnycoding

import cats.effect._
import io.chrisdavenport.log4cats.Logger
import io.chrisdavenport.log4cats.slf4j._
import org.http4s.blaze.server.BlazeServerBuilder
import xyz.funnycoding.modules._

import scala.concurrent.ExecutionContext

object Main extends IOApp {

  implicit val logger = Slf4jLogger.getLogger[IO]

  import com.sksamuel.elastic4s.cats.effect.instances._

  override def run(args: List[String]): IO[ExitCode] =
    config.load[IO].flatMap { cfg =>
      Logger[IO].info(s"Loaded config $cfg") *>
        AppResources.make[IO](cfg).use { res =>
          res.els.use { els =>
            for {
              algebras <- Algebras.make[IO](res.psql, res.client, els)
              api <- HttpApi.make[IO](algebras)
              _ <- Logger[IO].info("starting web server")
              _ <- BlazeServerBuilder[IO](ExecutionContext.global)
                    .bindHttp(
                      port = 8080,
                      host = "0.0.0.0"
                    )
                    .withHttpApp(api.httpApp)
                    .serve
                    .compile
                    .drain
            } yield ExitCode.Success
          }
        }
    }

}
