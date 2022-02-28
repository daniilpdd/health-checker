package hc.services.checker

import hc.data.{Check, StatusCode, Timeout}
import zhttp.http.{Method, URL}
import zhttp.service.Client.ClientRequest
import zhttp.service.{ChannelFactory, Client, EventLoopGroup}
import zio.clock.{Clock, currentTime}
import zio.duration.durationInt
import zio.{RIO, URLayer, ZIO}

import java.util.Date
import java.util.concurrent.TimeUnit

final class CheckerZHttp(client: Client) extends Checker.Service {
  override def check(url: String): RIO[Clock, Check] = {
    for {
      _url <- ZIO.fromEither(URL.fromString(url))
      startTime <- currentTime(TimeUnit.MILLISECONDS)
      res <- client.request(ClientRequest(Method.GET, _url)).timeoutTo(Timeout)(c => StatusCode(c.status.asJava.code()))(2.second)
      finishTime <- currentTime(TimeUnit.MILLISECONDS)
    } yield Check(url, finishTime - startTime, new Date(startTime), res)
  }
}

object CheckerZHttp {
  lazy val live: URLayer[EventLoopGroup with ChannelFactory, Checker] =
    Client.make.map(new CheckerZHttp(_)).toLayer
}
