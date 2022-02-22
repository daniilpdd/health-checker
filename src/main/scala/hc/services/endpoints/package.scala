package hc.services

import hc.data.Endpoint
import hc.system.config.{Config, JsonConfig}
import zio.json.DecoderOps
import zio.{Has, RIO, ULayer, ZIO, ZLayer}

import java.nio.file.Files

package object endpoints {
  type Endpoints = Has[Endpoints.Service]

  object Endpoints {
    trait Service {
      def load: RIO[Has[JsonConfig], Set[Endpoint]]
    }

    val live: ULayer[Endpoints] = ZLayer.succeed(new Service {
      override def load: RIO[Has[JsonConfig], Set[Endpoint]] = {
        for {
          config <- Config.jsonConfig
          data <- ZIO.effect(Files.readString(java.nio.file.Paths.get(config.path)))
          endpoints <- ZIO.fromEither(data.fromJson[Set[Endpoint]]).mapError(s => new RuntimeException(s))
        } yield endpoints
      }
    })

    def load: RIO[Endpoints with Has[JsonConfig], Set[Endpoint]] = ZIO.accessM(_.get.load)
  }
}
