ThisBuild / scalaVersion := "2.13.15"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "org.daniilpdd"

val zhttpVersion = "1.0.0.0-RC23"
val zioVersion = "1.0.13"
val zioLoggingVersion = "0.5.14"
val doobieVersion = "1.0.0-RC1"
val catsV = "2.7.0"
val catsEffectV = "3.3.5"
val interopVersion = "3.2.9.1"
val configVersion = "0.17.1"
val refinedV = "0.9.28"
val zioJsonVersion = "0.2.0-M3"
val zioMetrics = "1.0.14"
val zioMagic = "0.3.11"

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

Compile / mainClass := Some("hc.Main")

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
      "ch.qos.logback" % "logback-classic" % "1.2.10",
      "org.typelevel" %% "cats-core" % catsV,
      "org.typelevel" %% "cats-effect" % catsEffectV,
      "dev.zio" %% "zio-interop-cats" % interopVersion,
      "com.github.pureconfig" %% "pureconfig" % configVersion,
      "eu.timepit" %% "refined-pureconfig" % refinedV,
      "dev.zio" %% "zio-json-yaml" % zioJsonVersion,
      "dev.zio" %% "zio-metrics-prometheus" % zioMetrics,
      "io.github.kitlangton" %% "zio-magic" % zioMagic
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
