package hc.persistence

import hc.data.Check
import zio.{Has, RIO, Task}

import java.util.Date

package object check {
  type CheckDao = Has[CheckDao.Service]

  object CheckDao {
    trait Service {
      def create(check: Check): Task[Check]
      def getByUrl(url: String): Task[Seq[Check]]
      def getByUrlAndDate(url: String, beginDate: Date, endDate: Date): Task[Seq[Check]]
    }

    def create(check: Check): RIO[CheckDao, Check] =
      RIO.accessM(_.get.create(check))
    def getByUrl(url: String): RIO[CheckDao, Seq[Check]] =
      RIO.accessM(_.get.getByUrl(url))
    def getByUrlAndDate(url: String, beginDate: Date, endDate: Date): RIO[CheckDao, Seq[Check]] =
      RIO.accessM(_.get.getByUrlAndDate(url, beginDate, endDate))
  }
}
