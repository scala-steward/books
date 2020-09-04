package xyz.funnycoding.gc

final case class GCounter(counters: Map[String, Int]) {
  def increment(machine: String, amount: Int): GCounter = {
    val value = counters.getOrElse(machine, 0)
    GCounter(counters + (machine -> value))
  }

  def merge(that: GCounter): GCounter =
    GCounter(that.counters ++ counters.map {
          case (key: String, v: Int) =>
            val thatAmount = that.counters.getOrElse(key, 0)
            (key, Integer.max(v, thatAmount))
        })

  def total: Int = counters.values.sum
}
