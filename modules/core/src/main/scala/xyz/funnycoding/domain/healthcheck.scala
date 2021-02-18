package xyz.funnycoding.domain
import io.estatico.newtype.macros._

object healthcheck {
  @newtype case class ElasticSearchStatus(value: Boolean)
  @newtype case class PostgresStatus(value: Boolean)

  case class AppStatus(
      elasticSearch: ElasticSearchStatus,
      postgres: PostgresStatus
  )
}
