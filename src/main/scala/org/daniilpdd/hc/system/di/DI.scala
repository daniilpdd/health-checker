package org.daniilpdd.hc.system.di

//import hc.persistence.check.{CheckPersistence, DummyPersistence}

import org.daniilpdd.hc.services.checker.{Checker, CheckerDummy, CheckerZHttp}
import org.daniilpdd.hc.services.endpoints.{Endpoints, EndpointsDummy}
import org.daniilpdd.hc.persistence.check.CheckPersistence
import org.daniilpdd.hc.services.healthchecker.HealthChecker
import org.daniilpdd.hc.system.config.Config
import org.daniilpdd.hc.system.logger.Logger
import zhttp.service.{ChannelFactory, EventLoopGroup}
import zio.{TaskLayer, ZLayer}
import zio.clock.Clock
import zio.console.Console
import zio.logging.Logging
import zio.magic._
import zio.metrics.prometheus.Registry
import zio.metrics.prometheus.exporters.Exporters

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

  type AppEnv =
    HealthChecker with Checker with CheckPersistence with Clock with Endpoints with Config with Logging

  val testAppEnv: TaskLayer[TestAppEnv] =
    logger ++
      checker ++
      config ++
      healthChecker ++
      Clock.live ++ Console.live ++
      endpoints

  val magicDI = ZLayer.wire[AppEnv](
    Config.live,
    HealthChecker.live,
    Registry.live,
    Exporters.live,
    CheckPersistence.live,
    CheckerZHttp.live,
    Clock.live,
    Endpoints.live,
    Logger.live,
    EventLoopGroup.auto(),
    ChannelFactory.auto
  )

}
