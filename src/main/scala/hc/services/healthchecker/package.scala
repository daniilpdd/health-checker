package hc.services

import hc.data.Endpoint
import hc.persistence.check.CheckDao
import hc.services.checker.Checker
import zio.clock.Clock
import zio.duration.durationLong
import zio.{Has, RIO, Schedule, ULayer, ZIO, ZLayer}

package object healthchecker {

  type HealthChecker = Has[HealthChecker.Service]

  object HealthChecker {
    trait Service {
      def check(endpoint: Endpoint): RIO[Checker with CheckDao with Clock, Unit]
    }

    val live: ULayer[HealthChecker] = ZLayer.succeed(new Service {
      override def check(endpoint: Endpoint): RIO[Checker with CheckDao with Clock, Unit] = {
        val checkAndSave = for {
          check <- Checker.check(endpoint.url)
          _ <- CheckDao.create(check)
        } yield ()

        checkAndSave.schedule(Schedule.fixed(endpoint.period.millis)).unit
      }
    })

    def check(endpoint: Endpoint): ZIO[HealthChecker with Checker with CheckDao with Clock, Throwable, Unit] =
      ZIO.accessM(_.get.check(endpoint))
  }
}