package org.daniilpdd.healthchecker

import zio.{Scope, ZIO, ZIOAppArgs, ZIOAppDefault}

object MainApp extends ZIOAppDefault:
  override def run: ZIO[Any with ZIOAppArgs with Scope, Any, Any] = ZIO.succeed(println("kek"))
