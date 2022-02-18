package hc.system

import pureconfig.generic.auto._
import pureconfig.ConfigSource
import zio.logging.{Logging, log}
import zio.{Has, Task, URIO, ZIO, ZLayer}

package object config {
  type Config = Has[PostgresConfig]

  object Config {
    private lazy val source = ConfigSource.default

    val live: ZLayer[Logging, Throwable, Config] =
      Task
        .effect(source.loadOrThrow[Configuration])
        .map(cfg => cfg.postgresConfig)
        .tapBoth(err => log.error(s"Error loading config $err"), cfg => log.info(s"Successful loading config: $cfg"))
        .toLayer

    val dbConfig: URIO[Has[PostgresConfig], PostgresConfig] = ZIO.service
  }
}
