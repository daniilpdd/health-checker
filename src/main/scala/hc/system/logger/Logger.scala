package hc.system.logger

import zio.clock.Clock
import zio.console.Console
import zio.logging.Logging
import zio.logging.slf4j.Slf4jLogger
import zio.{ULayer, URLayer}

object Logger {
  val live: ULayer[Logging] =
    Slf4jLogger.make { (_, msg) =>
      msg
    }

  val console: URLayer[Console with Clock, Logging] = Logging.console()
}
