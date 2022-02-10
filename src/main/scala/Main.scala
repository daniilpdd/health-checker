import hc.checker.Checker
import zio._
import zio.console.putStrLn
import zio.console.Console

object Main extends App {
  val env: ULayer[Has[Checker.Service] with Console] = Checker.dummy ++ Console.live

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {
    val prog = for {
      delay <- Checker.check("https://www.google.com")
      _ <- putStrLn(delay.toString)
    } yield ()

    prog.provideLayer(env).exitCode
  }
}