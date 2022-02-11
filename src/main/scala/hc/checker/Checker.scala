package hc.checker

import zhttp.service.{ChannelFactory, EventLoopGroup}
import zio._

object Checker {
  type Checker = Has[Service]

  sealed trait CheckError
  case object TimeoutError extends CheckError
  case object NotAvailableError extends CheckError

  trait Service {
    def check(uri: String): IO[CheckError, Int]
  }

  val dummy: ULayer[Checker] = ZLayer.succeed(new Service {
    override def check(uri: String): IO[CheckError, Int] = ZIO.succeed(println(s"[print] $uri")).as(1)
  })

  def check(uri: String): ZIO[Checker, CheckError, Int] = ZIO.accessM[Checker](_.get.check(uri))
}
