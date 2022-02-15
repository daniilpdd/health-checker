ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val zhttpVersion = "1.0.0.0-RC23"
val zioVersion = "1.0.13"
val zioLoggingVersion = "0.5.14"

lazy val root = (project in file("."))
  .settings(
    name := "health-checker",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion,
      "dev.zio" %% "zio-test" % zioVersion % Test,
      "io.d11" %% "zhttp" % zhttpVersion,
      "io.d11" %% "zhttp" % zhttpVersion % Test,
      "dev.zio" %% "zio-logging" % zioLoggingVersion,
      "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion,
      "ch.qos.logback" % "logback-classic" % "1.2.10"
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
