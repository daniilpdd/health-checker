package org.daniilpdd.hc.services

import org.daniilpdd.hc.data.Endpoint
import org.daniilpdd.hc.persistence.check.CheckPersistence
import org.daniilpdd.hc.services.checker.Checker
import zio.clock.Clock
import zio.duration.durationLong
import zio.{Has, RIO, Schedule, ULayer, ZIO, ZLayer}

package object healthchecker {

  type HealthChecker = Has[HealthChecker.Service]

  object HealthChecker {
    trait Service {
      def check(endpoint: Endpoint): RIO[Checker with CheckPersistence with Clock, Unit]
    }

    val live: ULayer[HealthChecker] = ZLayer.succeed(new Service {
      override def check(endpoint: Endpoint): RIO[Checker with CheckPersistence with Clock, Unit] = {
        val checkAndSave = for {
          check <- Checker.check(endpoint.url)
          _ <- CheckPersistence.create(check)
        } yield ()

        checkAndSave.schedule(Schedule.fixed(endpoint.period.millis)).unit
      }
    })

    def check(endpoint: Endpoint): ZIO[HealthChecker with Checker with CheckPersistence with Clock, Throwable, Unit] =
      ZIO.accessM(_.get.check(endpoint))
  }
}
