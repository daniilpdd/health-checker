package hc.checker

import hc.checker.Checker.CheckError
import zio._
import zio.console.Console

final class CheckerDummy(console: Console.Service) extends Checker.Service {
  override def check(url: String): IO[CheckError, Long] =
    console.putStrLn(s"[dummy print] $url").mapBoth(_ => Checker.NotAvailableError, _ => 1L)
}

object CheckerDummy {
  val live: URLayer[Console, Checker] =
    ZLayer.fromService[Console.Service, Checker.Service](console => new CheckerDummy(console))
}
