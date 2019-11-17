val ScalatraVersion = "2.7.0-RC1"

organization := "com.github.rmaestroni"

name := "JSON validator"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.12.10"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.json4s"   %% "json4s-jackson" % "3.5.2",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.19.v20190610" % "container;compile",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "com.github.java-json-tools" % "json-schema-validator" % "2.2.11" % "compile"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)

enablePlugins(JavaAppPackaging)
// Remove the top level directory for universal package
topLevelDirectory := None
