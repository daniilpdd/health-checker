package hc.services.endpoints
import hc.data.Endpoint
import hc.system.config.JsonConfig
import zio.json.DecoderOps
import zio.{Has, RIO, ULayer, ZIO, ZLayer}

object EndpointsDummy {
  val live: ULayer[Endpoints] = ZLayer.succeed(new Endpoints.Service {
    override def load: RIO[Has[JsonConfig], Set[Endpoint]] = for {
      str <- ZIO.succeed(endpointsJson)
      en <- ZIO.fromEither(str.fromJson[Set[Endpoint]]).mapError(s => new Throwable(s))
    } yield en
  })

  private val endpointsJson ="""
      |[{
      | "url": "https://www.google.com",
      | "period": 1500
      |},
      |{
      | "url": "https://www.yandex.ru",
      | "period": 2000
      |},
      |{
      | "url": "https://habr.com",
      | "period": 1000
      |},
      |{
      | "url": "https://vk.com",
      | "period": 1000
      |},
      |{
      | "url": "https://zio.dev",
      | "period": 3000
      |},
      |{
      | "url": "https://zi1o.dev",
      | "period": 3000
      |}
      |]
      |""".stripMargin
}
