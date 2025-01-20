package org.daniilpdd.healthchecker

import org.daniilpdd.healthchecker.model.HCConfig
import zio.config.*
import zio.config.magnolia.deriveConfig
import zio.config.typesafe.TypesafeConfigProvider
import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}


object MainApp extends ZIOAppDefault:
  val program = for {
    descriptor <- ZIO.attempt(deriveConfig[HCConfig])
    c <- ZIO.attempt(descriptor.from(TypesafeConfigProvider.fromResourcePathZIO()))
    conf <- ZIO.attempt(c.config)
    _ <- ZIO.succeed(conf.map(println))
  } yield ()

  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] =
    program

end MainApp


