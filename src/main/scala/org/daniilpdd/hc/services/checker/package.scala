package org.daniilpdd.hc.services

import org.daniilpdd.hc.data.Check
import zio.clock.Clock
import zio.duration.durationInt
import zio.{Has, RIO, Schedule}

package object checker {
  type Checker = Has[Checker.Service]

  object Checker {
    trait Service {
      def check(url: String): RIO[Clock, Check]
    }

    def check(url: String): RIO[Checker with Clock, Check] = RIO.accessM(_.get.check(url))
  }
}