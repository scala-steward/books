package xyz.funnycoding.algebras

import cats.effect.Sync
import xyz.funnycoding.domain.data.Employee

trait Employees[F[_]] {
  def findAll(): F[List[Employee]]
}

final class LiveEmployees[F[_]: Sync] extends Employees[F] {
  // TODO implement me
  override def findAll(): F[List[Employee]] = Sync[F].point(Nil)
}

object LiveEmployees {
  def make[F[_]: Sync](): F[Employees[F]] = Sync[F].delay {
    new LiveEmployees[F]
  }
}