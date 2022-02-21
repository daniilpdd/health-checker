package hc.endpoints

import hc.data.Endpoint
import hc.system.config.{Config, YamlConfig}
import zio.json.DecoderOps
import zio.{Has, Task, URLayer, ZIO}

import java.nio.file.Files

final class EndpointsFromJson(path: String) extends Endpoints.Service {
  override def load: Task[Seq[Endpoint]] =
    for {
      data <-  ZIO.effect(Files.readString(java.nio.file.Paths.get(path)))
      endpoints <- ZIO.fromEither(data.fromJson[Seq[Endpoint]]).mapError(s => new RuntimeException(s))
    } yield endpoints
}

object EndpointsFromJson {
  val live: URLayer[Has[YamlConfig], Endpoints] =  Config.yamlConfig.map(cfg => new EndpointsFromJson(cfg.path)).toLayer
}
