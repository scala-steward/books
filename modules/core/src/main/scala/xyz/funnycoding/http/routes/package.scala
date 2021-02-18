package xyz.funnycoding.http

import eu.timepit.refined.types.string.NonEmptyString

package object routes {
  object NonEmptyStringPathVar {
    def unapply(arg: String): Option[NonEmptyString] =
      NonEmptyString.from(arg).toOption
  }
}
