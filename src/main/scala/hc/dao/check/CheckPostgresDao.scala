package hc.dao.check

import doobie.util.transactor.Transactor
import hc.data.Check
import hc.system.dbtransactor.DBTransactor
import zio.{Task, URLayer}

import java.util.Date

final class CheckPostgresDao(trx: Transactor[Task]) extends CheckDao.Service {
  override def create(check: Check): Task[Check] = ???

  override def getByUrl(url: String): Task[Seq[Check]] = ???

  override def getByUrlAndDate(url: String, beginDate: Date, endDate: Date): Task[Seq[Check]] = ???
}

object CheckPostgresDao{
  val live: URLayer[DBTransactor, CheckDao] = DBTransactor.transactor.map(new CheckPostgresDao(_)).toLayer

}
