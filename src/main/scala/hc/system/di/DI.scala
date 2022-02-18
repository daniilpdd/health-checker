package hc.system.di

import hc.checker.{Checker, CheckerZHttp}
import hc.system.config.Config
import hc.system.dbtransactor.DBTransactor
import hc.system.logger.Logger
import zhttp.service.{ChannelFactory, EventLoopGroup}
import zio.Layer
import zio.clock.Clock
import zio.logging.Logging

object DI {
  val logger = Logger.live
  val config = logger >>> Config.live
  val transactor = config ++ logger >>> DBTransactor.live
  val zHttp = ChannelFactory.auto ++ EventLoopGroup.auto() ++ Clock.live >>> CheckerZHttp.live


  val appEnv: Layer[Throwable, Logging with Checker] =
    logger ++
//      config ++
//      transactor ++
      zHttp
}
