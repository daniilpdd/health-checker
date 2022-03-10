package hc.system.di

//import hc.persistence.check.{CheckPersistence, DummyPersistence}

import hc.services.checker.{Checker, CheckerDummy, CheckerZHttp}
import hc.services.endpoints.{Endpoints, EndpointsDummy}
import hc.services.healthchecker.HealthChecker
import hc.system.config.Config
import hc.system.logger.Logger
import zhttp.service.{ChannelFactory, EventLoopGroup}
import zio.{TaskLayer, ZLayer}
import zio.clock.Clock
import zio.console.Console
import zio.logging.Logging
import zio.magic._

object DI {
  val logger = Logger.live
  val config = logger >>> Config.live
  val checker = ChannelFactory.auto ++ EventLoopGroup.auto() ++ Clock.live >>> CheckerZHttp.live
  val endpoints = Endpoints.live
  val healthChecker = HealthChecker.live
  val dummyEndpoints = EndpointsDummy.live

  val kek = Console.live >>> CheckerDummy.live

  type TestAppEnv =
    Logging with
      Checker with
      Config with
      HealthChecker with
      Endpoints with
      Console with
      Clock

  val testAppEnv: TaskLayer[TestAppEnv] =
    logger ++
      checker ++
      config ++
      healthChecker ++
      Clock.live ++ Console.live ++
      endpoints

  val magicDI = ZLayer.wire[TestAppEnv](
    Logger.live,
    Config.live,
    ChannelFactory.auto,
    EventLoopGroup.auto(),
    CheckerZHttp.live,
    Endpoints.live,
    HealthChecker.live,
    EndpointsDummy.live
  )

}
