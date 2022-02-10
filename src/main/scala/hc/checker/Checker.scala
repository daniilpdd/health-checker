package hc.checker

import zio._

object Checker {
  type Checker = Has[Service]

  trait Service {

  }
}
