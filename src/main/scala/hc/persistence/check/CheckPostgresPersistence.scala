package hc.persistence.check

import doobie.Update0
import doobie.implicits._
import doobie.util.query.Query0
import doobie.util.transactor.Transactor
import hc.data.Check
import hc.persistence.check.CheckPostgresPersistence.Sql
import hc.system.dbtransactor.DBTransactor
import zio.interop.catz._
import zio.{Task, URLayer}

import java.text.SimpleDateFormat
import java.util.Date

final class CheckPostgresPersistence(trx: Transactor[Task]) extends CheckPersistence.Service {
  override def create(check: Check): Task[Check] =
    Sql
      .create(check)
      .run
      .transact(trx)
      .as(check)

  override def getByUrl(url: String): Task[Seq[Check]] =
    Sql
      .getByUrl(url)
      .to[Seq]
      .transact(trx)

  override def getByUrlAndPeriod(url: String, beginDate: Date, endDate: Date): Task[Seq[Check]] =
    Sql
      .getByUrlAndPeriod(url, beginDate, endDate)
      .to[Seq]
      .transact(trx)
}

object CheckPostgresPersistence {

  object Sql {
    private val table_name = "checks"

    val sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    def create(check: Check): Update0 =
      sql"""insert into $table_name (url, timeout, time)
            values (${check.url}, ${check.timeout}, ${check.time})""".update

    def getByUrl(url: String): Query0[Check] =
      sql"""select * from $table_name
            where url = $url""".query[Check]

    def getByUrlAndPeriod(url: String, beginDate: Date, endDate: Date): Query0[Check] =
      sql"""select * from $table_name
            where url = $url and
            "time" between ${sqlDateFormat.format(beginDate)} and ${sqlDateFormat.format(endDate)}""".query[Check]
  }

  val live: URLayer[DBTransactor, CheckPersistence] = DBTransactor.transactor.map(new CheckPostgresPersistence(_)).toLayer
}
