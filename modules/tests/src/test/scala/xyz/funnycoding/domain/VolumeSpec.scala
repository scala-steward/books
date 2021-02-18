package xyz.funnycoding.domain

import suite.PureTestSuite
import cats.effect.IO
import xyz.funnycoding.arbitrairies._
import xyz.funnycoding.domain.volume._

final class VolumeSpec extends PureTestSuite {
  forAll { (volumesSearch: VolumesSearch) =>
    spec("generate query") {
      IO {
        assert(VolumesSearch.query(volumesSearch).count(_ == '+') == volumesSearch.size - 1)
      }
    }
  }
}
