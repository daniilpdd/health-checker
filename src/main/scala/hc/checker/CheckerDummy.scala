package hc.checker

import zio._
import zio.console.Console

final class CheckerDummy(console: Console.Service) extends Checker.Service {
  override def check(url: String): Task[Long] =
    console.putStrLn(s"[dummy print] $url").as(1L)
}

object CheckerDummy {
  val live: URLayer[Console, Checker] =
    ZLayer.fromService[Console.Service, Checker.Service](console => new CheckerDummy(console))
}
