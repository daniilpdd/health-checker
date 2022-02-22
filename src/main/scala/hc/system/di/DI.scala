package hc.system.di

import hc.persistence.check.{CheckPersistence, DummyPersistence}
import hc.services.checker.{Checker, CheckerZHttp}
import hc.services.endpoints.{Endpoints, EndpointsDummy}
import hc.services.healthchecker.HealthChecker
import hc.system.config.Config
import hc.system.dbtransactor.DBTransactor
import hc.system.logger.Logger
import zhttp.service.{ChannelFactory, EventLoopGroup}
import zio.TaskLayer
import zio.clock.Clock
import zio.console.Console
import zio.logging.Logging

object DI {
  val logger = Logger.live
  val config = logger >>> Config.live
  val transactor = config ++ logger >>> DBTransactor.live
  val checker = ChannelFactory.auto ++ EventLoopGroup.auto() ++ Clock.live >>> CheckerZHttp.live
  val endpoints = Endpoints.live
  val healthChecker = HealthChecker.live
  val dummyDao = Console.live >>> DummyPersistence.live
  val dummyEndpoints = EndpointsDummy.live

  type TestAppEnv =
    Logging with
    Checker with
    Config with
    HealthChecker with
    CheckPersistence with
    Endpoints with
    Console with
    Clock

  val testAppEnv: TaskLayer[TestAppEnv] =
    logger ++
      checker ++
      config ++
      healthChecker ++
      dummyDao ++
      Clock.live ++ Console.live ++
      endpoints

}
