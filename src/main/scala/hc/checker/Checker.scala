package hc.checker

import zio._

object Checker {
  type CheckerEnv = Has[Service]

  sealed trait CheckError
  case object TimeoutError extends CheckError
  case object NotAvailableError extends CheckError

  trait Service {
    def check(uri: String): IO[CheckError, Int]
  }

  val dummy: ULayer[Has[Service]] = ZLayer.succeed(new Service {
    override def check(uri: String): IO[CheckError, Int] = ZIO.succeed(println(s"[print] $uri")).as(1)
  })

  def check(uri: String): ZIO[CheckerEnv, CheckError, Int] = ZIO.accessM[CheckerEnv](_.get.check(uri))
}
