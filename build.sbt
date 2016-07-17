name := "EntertainmentPlaces2Gis"

version := "1.0"

scalaVersion := "2.11.8"

val akkaVersion  = "2.4.8"

def commons = Seq(
  "com.typesafe" % "config" % "1.3.0"
)

def logging = Seq(
  "ch.qos.logback"             %  "logback-classic" % "1.1.7",
  "com.typesafe.scala-logging" %% "scala-logging"   % "3.4.0"
)

def akka = Seq(
  "com.typesafe.akka" %% "akka-actor"                        % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"                       % akkaVersion,
  "com.typesafe.akka" %% "akka-http-experimental"            % akkaVersion
)

def json = Seq(
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
)

def testDependencies = Seq(
  "org.scalatest"     %% "scalatest"           % "2.2.6"     % "test",
  "com.typesafe.akka" %% "akka-http-testkit"   % akkaVersion % "test"
)

libraryDependencies ++= ( commons ++ logging ++ akka ++ json ++ testDependencies )