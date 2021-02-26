package xyz.funnycoding

import cats.data.NonEmptyList
import eu.timepit.refined.api.Refined
import eu.timepit.refined.types.string.NonEmptyString
import org.scalacheck.Gen
import xyz.funnycoding.domain.companies._
import xyz.funnycoding.domain.volume._

object generators {

  val genNonEmptyString: Gen[NonEmptyString] =
    Gen
      .chooseNum(21, 40)
      .flatMap { n =>
        Gen.buildableOfN[String, Char](n, Gen.alphaChar)
      }
      .map[NonEmptyString](Refined.unsafeApply)

  private def genNonEmptyList[A](g: Gen[A]): Gen[NonEmptyList[A]] =
    Gen
      .nonEmptyListOf(g)
      .map[NonEmptyList[A]](NonEmptyList.fromListUnsafe)

  implicit val bookGen: Gen[Book] = for {
    uuid <- Gen.uuid
    name <- genNonEmptyString
  } yield Book(uuid, name)

  implicit val createBookGen: Gen[CreateBook] = for {
    name <- genNonEmptyString
  } yield CreateBook(name)

  implicit val inAuthorGen: Gen[InAuthor] = for {
    nes <- genNonEmptyString
  } yield InAuthor(nes)

  implicit val inTitleGen: Gen[Intitle] = for {
    nes <- genNonEmptyString
  } yield Intitle(nes)

  implicit val inPublisherGen: Gen[InPublisher] = for {
    nes <- genNonEmptyString
  } yield InPublisher(nes)

  implicit val inCategoryGen: Gen[InCategory] = for {
    nes <- genNonEmptyString
  } yield InCategory(nes)

  implicit val volumeSearchGen: Gen[VolumeSearch] = for {
    vs <- Gen.oneOf(inAuthorGen, inTitleGen, inPublisherGen, inCategoryGen)
  } yield vs

  implicit val volumesSearchGen: Gen[VolumesSearch] = for {
    volumeSearchNel <- genNonEmptyList(volumeSearchGen)
  } yield volumeSearchNel
}
