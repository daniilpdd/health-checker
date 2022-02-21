package hc

import hc.checker.Checker
import hc.data.Endpoint
import hc.system.di.DI
import zio.clock.Clock
import zio.console._
import zio.duration.durationLong
import zio.json.DecoderOps
import zio.logging.log
import zio.{App, ExitCode, Schedule, URIO, ZIO}

object Main extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val kek = ZIO.succeed(
      """
        |[{
        | "url": "https://www.google.com",
        | "period": 1500
        |},
        |{
        | "url": "https://www.yandex.ru",
        | "period": 2000
        |},
        |{
        | "url": "https://habr.com",
        | "period": 1000
        |},
        |{
        | "url": "https://zio.dev",
        | "period": 3000
        |},
        |{
        | "url": "https://zi1o.dev",
        | "period": 3000
        |}
        |]
        |""".stripMargin)

    val endpoints = for {
      str <- kek
      en <- ZIO.fromEither(str.fromJson[Set[Endpoint]]).mapError(s => new RuntimeException(s))
      _ <- putStrLn(en.toString())
    } yield en

    val program = for {
      e <- endpoints
      delay <- ZIO.foreachPar(e) { a =>
        (for {
          d <- Checker.check(a.url)
          _ <- putStrLn(s"[${Thread.currentThread().getName}] ${a.url} - ${d.toString}")
        } yield d).schedule(Schedule.fixed(a.period.millis)).fork
      }
      _ <- ZIO.foreachPar_(delay)(_.join)
    } yield ()

    program
      .tapError(err => log.error(err.toString))
      .provideLayer(DI.testAppEnv ++ Clock.live ++ Console.live)
      .exitCode
  }
}
