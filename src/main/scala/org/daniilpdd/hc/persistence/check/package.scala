package org.daniilpdd.hc.persistence

import org.daniilpdd.hc.data.Check
import org.daniilpdd.hc.system.config.PrometheusConfig
import zio.metrics.prometheus.Registry
import zio.metrics.prometheus.exporters.Exporters
import zio.{Has, RIO, Task, URLayer, ZLayer}

package object check {
  type CheckPersistence = Has[CheckPersistence.Service]

  object CheckPersistence {
    trait Service {
      def create(check: Check): Task[Check]
    }

    val live: URLayer[Has[PrometheusConfig] with Has[Registry.Service] with Has[Exporters.Service], CheckPersistence] = {
      ZLayer.fromServices[PrometheusConfig, Registry.Service, Exporters.Service, CheckPersistence.Service] { (cfg, reg, exp) =>
        new CheckPersistence.Service {
          override def create(check: Check): Task[Check] =
            for {
              r <- reg.getCurrent()
              _ <- exp.initializeDefaultExports(r)
              _ <- exp.pushGateway(r, cfg.host, cfg.port, cfg.jobName, None, None, None)
            } yield (check)
        }
      }
    }

    def create(check: Check): RIO[CheckPersistence, Check] =
      RIO.accessM(_.get.create(check))
  }
}
