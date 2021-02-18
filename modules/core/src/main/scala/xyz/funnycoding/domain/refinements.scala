package xyz.funnycoding.domain

import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex

object refinements {
  type EmailPred = MatchesRegex[W.`"""(?=[^\\s]+)(?=(\\w+)@([\\w\\.]+))"""`.T]
  type Email = String Refined EmailPred
}
