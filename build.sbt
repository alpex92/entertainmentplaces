name := "EntertainmentPlaces2Gis"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion  = "2.4.3"

def commons = Seq(
  "com.typesafe" % "config" % "1.3.0"
)

def akka = Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion
)

def testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

libraryDependencies ++= ( commons ++ akka ++ testDependencies )