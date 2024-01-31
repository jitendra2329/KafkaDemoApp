ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "KafkaDemoApp"
  )


val kakfaversion ="3.1.0"

libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka-streams-scala"
).map(_ % kakfaversion)

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)