name := "ScalaWeb"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "commons-logging" % "commons-logging" % "1.1.1"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1.2"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-feature")
