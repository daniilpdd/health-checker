package org.daniilpdd.hc.services.checker

import org.daniilpdd.hc.data.{Check, StatusCode}
import zio.clock.{Clock, currentTime}
import zio.console.Console
import zio.{RIO, URLayer, ZLayer}

import java.util.Date
import java.util.concurrent.TimeUnit

final class CheckerDummy(console: Console.Service) extends Checker.Service {
  override def check(url: String): RIO[Clock, Check] =
    for {
      s <- currentTime(TimeUnit.MILLISECONDS)
      _ <- console.putStrLn(s"[dummy print] $url")
      f <- currentTime(TimeUnit.MILLISECONDS)
    } yield Check(url, f - s, new Date(s), StatusCode(200))
}

object CheckerDummy {
  val live: URLayer[Console, Checker] =
    ZLayer.fromService[Console.Service, Checker.Service](console => new CheckerDummy(console))
}
