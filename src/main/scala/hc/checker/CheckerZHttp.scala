package hc.checker

import zhttp.http.{Method, URL}
import zhttp.service.Client.ClientRequest
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.clock.Clock
import zio.duration.durationInt
import zio.{Task, URIO, ZIO, ZLayer}

import java.util.concurrent.TimeUnit

final class CheckerZHttp(client: Client, clock: Clock.Service) extends Checker.Service {
  override def check(url: String): Task[Long] = {
    for {
      url <- ZIO.fromEither(URL.fromString(url))
      startTime <- clock.currentTime(TimeUnit.MILLISECONDS)
      _ <- client.request(ClientRequest(Method.GET, url)).timeout(2.second).provideLayer(ZIO.succeed(clock).toLayer)
      finishTime <- clock.currentTime(TimeUnit.MILLISECONDS)
    } yield finishTime - startTime
  }
}

object CheckerZHttp {
  lazy val live: ZLayer[EventLoopGroup with ChannelFactory with Clock, Nothing, Checker] = {
    val zioChecker: URIO[Clock with EventLoopGroup with ChannelFactory, CheckerZHttp] = for {
      client <- Client.make
      a <- ZIO.service[Clock.Service]
    } yield new CheckerZHttp(client, a)

    zioChecker.toLayer
  }
}
