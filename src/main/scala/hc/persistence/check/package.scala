package hc.persistence

import hc.data.Check
import zio.{Has, RIO, Task}

import java.util.Date

package object check {
  type CheckPersistence = Has[CheckPersistence.Service]

  object CheckPersistence {
    trait Service {
      def create(check: Check): Task[Check]
      def getByUrl(url: String): Task[Seq[Check]]
      def getByUrlAndDate(url: String, beginDate: Date, endDate: Date): Task[Seq[Check]]
    }

    def create(check: Check): RIO[CheckPersistence, Check] =
      RIO.accessM(_.get.create(check))
    def getByUrl(url: String): RIO[CheckPersistence, Seq[Check]] =
      RIO.accessM(_.get.getByUrl(url))
    def getByUrlAndDate(url: String, beginDate: Date, endDate: Date): RIO[CheckPersistence, Seq[Check]] =
      RIO.accessM(_.get.getByUrlAndDate(url, beginDate, endDate))
  }
}
