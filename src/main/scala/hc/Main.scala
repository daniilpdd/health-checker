package hc

import hc.services.endpoints.Endpoints
import hc.services.healthchecker.HealthChecker
import hc.system.di.DI
import zio.logging.log
import zio.{App, ExitCode, URIO, ZIO}

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
      .provideLayer(DI.testAppEnv)
      .exitCode
  }
}
