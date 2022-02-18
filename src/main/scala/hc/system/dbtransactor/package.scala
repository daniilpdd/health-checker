package hc.system

import doobie.hikari.HikariTransactor
import doobie.util.transactor.Transactor
import hc.system.config.{Config, PostgresConfig}
import zio.{Has, Managed, Task, URIO, ZIO, ZLayer, ZManaged}
import zio.interop.catz.implicits._
import zio.interop.catz._
import zio.logging.{Logging, log}

import scala.concurrent.ExecutionContext

package object dbtransactor {
  type DBTransactor = Has[Transactor[Task]]

  object DBTransactor {
    private def makeTransactor(
                                conf: PostgresConfig,
                                connectEC: ExecutionContext
                              ): Managed[Throwable, Transactor[Task]] =
      HikariTransactor.newHikariTransactor[Task](
        conf.className,
        conf.url,
        conf.user,
        conf.password,
        connectEC
      ).toManagedZIO

    val managed: ZManaged[Has[PostgresConfig] with Logging, Throwable, Transactor[Task]] =
      for {
        config     <- Config.dbConfig.toManaged_
        connectEC  <- ZIO.descriptor.map(_.executor.asEC).toManaged_
        _          <- log.info("Initializing DB Transactor and creating connection pool.").toManaged_
        transactor <- makeTransactor(config, connectEC)
      } yield transactor

    val live: ZLayer[Has[PostgresConfig] with Logging, Throwable, DBTransactor] = ZLayer.fromManaged(managed)

    val transactor: URIO[DBTransactor, Transactor[Task]] = ZIO.service
  }
}
