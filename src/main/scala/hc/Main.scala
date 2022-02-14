package hc

import hc.checker.{Checker, CheckerDummy, CheckerZHttp}
import zhttp.service.{ChannelFactory, EventLoopGroup}
import zio.clock.Clock
import zio.console.{Console, putStrLn}
import zio.{App, ExitCode, ULayer, URIO}

object Main extends App {
  val env: ULayer[Checker with Console] =
    ChannelFactory.auto ++
      EventLoopGroup.auto() ++
      Clock.live >>> CheckerZHttp.live ++
      Console.live

  val dummyEnv: ULayer[Checker with Console] = Console.live >>> CheckerDummy.live ++ Console.live

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val program = for {
      delay <- Checker.check("https://www.google.com")
      _ <- putStrLn(s"[${Thread.currentThread().getName}] - ${delay.toString}")
    } yield ()

    val bothEnvTest = for {
      fiber <- program.provideLayer(env).fork
      _ <- program.provideLayer(dummyEnv)
      _ <- fiber.join
    } yield ()


    bothEnvTest.exitCode
  }
}
