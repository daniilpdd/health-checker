package hc

import zio.{Has, IO, ZIO}

package object checker {
  type Checker = Has[Checker.Service]

  object Checker {
    sealed trait CheckError
    case object TimeoutError extends CheckError
    case object NotAvailableError extends CheckError

    trait Service {
      def check(url: String): IO[CheckError, Long]
    }

    def check(url: String): ZIO[Checker, CheckError, Long] = ZIO.accessM[Checker](_.get.check(url))
  }
}