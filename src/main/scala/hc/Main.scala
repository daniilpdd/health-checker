package hc

import hc.checker.Checker
import hc.system.di.DI
import zio.logging.{Logging, log}
import zio.{App, ExitCode, URIO, ZIO}

object Main extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val program: ZIO[Logging with Checker, Throwable, Unit] = for {
      delay <- Checker.check("https://www.google.com")
      _ <- log.info(delay.toString)
    } yield ()

    program
      .tapError(err => log.error(err.toString))
      .provideLayer(DI.appEnv)
      .exitCode
  }
}
