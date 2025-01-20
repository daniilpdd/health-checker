package org.daniilpdd.healthchecker.service

import org.daniilpdd.healthchecker.model.Endpoint
import zio.UIO

object HealthChecker:
  def checkEndpoints(endpoints: Seq[Endpoint]): UIO[Any] = ???
