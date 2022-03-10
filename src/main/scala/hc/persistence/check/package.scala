package hc.persistence

import hc.data.Check
import zio.{Has, RIO, Task}

import java.util.Date

package object check {
  type CheckPersistence = Has[CheckPersistence.Service]

  object CheckPersistence {
    trait Service {
      def create(check: Check): Task[Check]
    }

    def create(check: Check): RIO[CheckPersistence, Check] =
      RIO.accessM(_.get.create(check))
  }
}
