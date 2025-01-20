scalaVersion := "3.6.2"
version := "0.1"
organization := "org.daniilpdd"
name := "healthchecker"

val zioVersion = "2.1.14"
val zioConfigVersion = "4.0.3"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-macros" % zioVersion,
  "dev.zio" %% "zio-config" % zioConfigVersion,
  "dev.zio" %% "zio-config-magnolia" % zioConfigVersion,
  "dev.zio" %% "zio-config-typesafe" % zioConfigVersion,
  "dev.zio" %% "zio-http" % "3.0.1"
)

scalacOptions += "-Ymacro-annotations"
