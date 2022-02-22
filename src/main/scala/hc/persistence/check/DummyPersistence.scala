package hc.persistence.check
import hc.data.Check
import zio.console.Console
import zio.{RLayer, Task, ZLayer}

import java.util.Date

class DummyPersistence(console: Console.Service) extends CheckDao.Service {
  override def create(check: Check): Task[Check] =
    console.putStrLn(s"Save check $check").as(check)

  override def getByUrl(url: String): Task[Seq[Check]] =
    console.putStrLn(s"Query result from db by url: Seq()").as(Seq[Check]())

  override def getByUrlAndDate(url: String, beginDate: Date, endDate: Date): Task[Seq[Check]] =
    console.putStrLn(s"Query result from db by url and dates: Seq()").as(Seq[Check]())
}

object DummyPersistence {
  val live: RLayer[Console, CheckDao] = ZLayer.fromService(new DummyPersistence(_))
}
