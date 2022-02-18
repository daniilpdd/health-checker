package hc.dao

import hc.data.Check
import zio.{Has, Task, ZIO}

import java.util.Date

package object check {
  type CheckDao = Has[CheckDao.Service]

  object CheckDao {
    trait Service {
      def create(check: Check): Task[Check]
      def getByUrl(url: String): Task[Seq[Check]]
      def getByUrlAndDate(url: String, beginDate: Date, endDate: Date): Task[Seq[Check]]
    }

    def create(check: Check): ZIO[CheckDao, Throwable, Check] =
      ZIO.accessM[CheckDao](_.get.create(check))
    def getByUrl(url: String): ZIO[CheckDao, Throwable, Seq[Check]] =
      ZIO.accessM[CheckDao](_.get.getByUrl(url))
    def getByUrlAndDate(url: String, beginDate: Date, endDate: Date): ZIO[CheckDao, Throwable, Seq[Check]] =
      ZIO.accessM[CheckDao](_.get.getByUrlAndDate(url, beginDate, endDate))
  }
}
