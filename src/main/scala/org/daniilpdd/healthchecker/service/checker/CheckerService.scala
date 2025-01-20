package org.daniilpdd.healthchecker.service.checker

import org.daniilpdd.healthchecker.model.{Check, Endpoint, StatusCode}
import zio.http.{Client, Request, URL}
import zio.{Clock, Task, ZIO}

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

trait CheckerService:
  def check(endpoint: Endpoint): Task[Check]

object CheckerService:
  def check(endpoint: Endpoint): ZIO[CheckerService, Throwable, Check] = ZIO.serviceWithZIO[CheckerService](_.check(endpoint))

case class ZIOHttpChecker(clock: Clock, client: Client) extends CheckerService:
  override def check(endpoint: Endpoint): Task[Check] = 
    for {
      startTime <- clock.currentTime(ChronoUnit.MILLIS)
      url <- ZIO.fromEither(URL.decode(endpoint.url))
      response <- client.url(url).batched(Request.get(endpoint.path))
      endTime <- clock.currentTime(ChronoUnit.MILLIS) 
    } yield Check(endpoint.url, endTime - startTime, LocalDateTime.now(), StatusCode(response.status.code)) //todo handle errors
  