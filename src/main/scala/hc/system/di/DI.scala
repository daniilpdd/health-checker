package hc.system.di

import hc.checker.{Checker, CheckerZHttp}
import hc.endpoints.{Endpoints, EndpointsFromJson}
import hc.system.config.Config
import hc.system.dbtransactor.DBTransactor
import hc.system.logger.Logger
import zhttp.service.{ChannelFactory, EventLoopGroup}
import zio.clock.Clock
import zio.logging.Logging
import zio.{Layer, ULayer}

object DI {
  val logger = Logger.live
  val config = logger >>> Config.live
  val transactor = config ++ logger >>> DBTransactor.live
  val checker = ChannelFactory.auto ++ EventLoopGroup.auto() ++ Clock.live >>> CheckerZHttp.live
  val endpoints = config >>> EndpointsFromJson.live

  val appEnv: Layer[Throwable, Logging with Config with DBTransactor with Checker with Endpoints] =
    logger ++
      config ++
      transactor ++
      checker ++
      endpoints

  val testAppEnv: ULayer[Logging with Checker] =
    logger ++
    checker
}
