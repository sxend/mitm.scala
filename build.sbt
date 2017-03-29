
organization := "arimitsu.sf"

name := "mitm.scala"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.bintrayRepo("sxend", "releases"),
  Resolver.bintrayRepo("sxend", "snapshots"),
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

libraryDependencies ++= {
  val akkaHttpVersion = "10.0.5"
  val shapelessVersion = "2.3.1"
  val spec2Version = "3.8.4"
  Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-jackson" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-xml" % akkaHttpVersion,
    "com.chuusai" %% "shapeless" % shapelessVersion,
    "org.specs2" %% "specs2-html" % spec2Version % "test",
    "org.specs2" %% "specs2-junit" % spec2Version % "test",
    "org.specs2" %% "specs2-core" % spec2Version % "test"
  )
}

publishMavenStyle := false

bintrayRepository := {
  if (version.value.matches("^[0-9]+\\.[0-9]+\\.[0-9]+$")) "releases" else "snapshots"
}

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

javacOptions ++= Seq("-source", "1.8")

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:reflectiveCalls",
  "-language:postfixOps"
)

assemblyMergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last endsWith "bnd.bnd" => MergeStrategy.first
  case x => (assemblyMergeStrategy in assembly).value(x)
}

testOptions in Test += Tests.Argument(TestFrameworks.Specs2, "junitxml", "html", "console")

assemblyJarName in assembly := s"scala-template-assembly-${version.value}.jar"

mainClass in assembly := Some("arimitsu.sf.template.Bootstrap")
