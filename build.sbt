ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val zhttpVersion = "1.0.0.0-RC23"

lazy val root = (project in file("."))
  .settings(
    name := "health-checker",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "1.0.13",
      "dev.zio" %% "zio-test" % "1.0.13" % Test,
      "io.d11" %% "zhttp" % zhttpVersion,
      "io.d11" %% "zhttp" % zhttpVersion % Test
    ),
    testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")
  )
