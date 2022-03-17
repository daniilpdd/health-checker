package hc

import hc.persistence.check.CheckPersistence
import hc.services.checker.Checker
import hc.services.endpoints.Endpoints
import hc.services.healthchecker.HealthChecker
import hc.system.config.JsonConfig
import hc.system.di.DI
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
