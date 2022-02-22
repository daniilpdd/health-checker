package hc.persistence.check

import doobie.util.transactor.Transactor
import hc.data.Check
import hc.system.dbtransactor.DBTransactor
import zio.{Task, URLayer}

import java.util.Date

final class CheckPostgresPersistence(trx: Transactor[Task]) extends CheckPersistence.Service {
  override def create(check: Check): Task[Check] = ???

  override def getByUrl(url: String): Task[Seq[Check]] = ???

  override def getByUrlAndDate(url: String, beginDate: Date, endDate: Date): Task[Seq[Check]] = ???
}

object CheckPostgresPersistence{
  val live: URLayer[DBTransactor, CheckPersistence] = DBTransactor.transactor.map(new CheckPostgresPersistence(_)).toLayer
}
