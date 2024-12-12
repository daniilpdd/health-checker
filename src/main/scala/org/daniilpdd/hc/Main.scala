package org.daniilpdd.hc

import org.daniilpdd.hc.persistence.check.CheckPersistence
import org.daniilpdd.hc.services.checker.Checker
import org.daniilpdd.hc.services.endpoints.Endpoints
import org.daniilpdd.hc.services.healthchecker.HealthChecker
import org.daniilpdd.hc.system.config.JsonConfig
import org.daniilpdd.hc.system.di.DI
import zio.clock.Clock
import zio.logging.log
import zio.{App, ExitCode, Has, URIO, ZIO}
import zio.magic._

object Main extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val program =
      for {
        endpoints <- Endpoints.load
        fibers <- ZIO.foreachPar(endpoints)(HealthChecker.check(_).fork)
        _ <- ZIO.foreachPar_(fibers)(_.join)
      } yield ()

    program
      .tapError(err => log.error(err.toString))
//      .provideLayer(DI.testAppEnv)
      /**
      Auto-construction cannot work with `someList: _*` syntax.
      Please pass the layers themselves into this method.
      .inject(DI.magicDI: _*)
       */
      .provideLayer(DI.magicDI)
      .exitCode
  }
}
