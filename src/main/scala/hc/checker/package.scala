package hc

import zio.{Has, RIO, Task, ZIO}

package object checker {
  type Checker = Has[Checker.Service]

  object Checker {
    trait Service {
      def check(url: String): Task[Long]
    }

    def check(url: String): RIO[Checker, Long] = ZIO.accessM[Checker](_.get.check(url))
  }
}