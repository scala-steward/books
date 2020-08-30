package xyz.funnycoding

import eu.timepit.refined.api.Refined
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Gen
import xyz.funnycoding.domain.data._

object generators {

  val genNonEmptyString: Gen[String] =
    Gen
      .chooseNum(21, 40)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }

  implicit val bookGen: Gen[Book] = for {
    uuid <- Gen.uuid
    name <- genNonEmptyString.map[NonEmptyString](Refined.unsafeApply)
  } yield Book(uuid, name)

  implicit val createBookGen: Gen[CreateBook] = for {
    name <- genNonEmptyString.map[NonEmptyString](Refined.unsafeApply)
  } yield CreateBook(name)
}
