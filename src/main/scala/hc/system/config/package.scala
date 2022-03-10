package hc.system

import pureconfig.generic.auto._
import pureconfig.ConfigSource
import zio.logging.{Logging, log}
import zio.{Has, Task, URIO, ZIO, ZLayer}

package object config {
  type Config = Has[JsonConfig]

  object Config {
    private lazy val source = ConfigSource.default

    val live: ZLayer[Logging, Throwable, Config] =
      Task
        .effect(source.loadOrThrow[Configuration])
        .tapBoth(err => log.error(s"Error loading config $err"), cfg => log.info(s"Successful loading config: $cfg"))
        .toLayer
        .map(c => Has(c.get.jsonConfig))

    val jsonConfig: URIO[Has[JsonConfig], JsonConfig] = ZIO.service
  }
}
