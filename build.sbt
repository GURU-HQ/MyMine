import AssemblyKeys._ 

name := "MyMine"

version := "0.0.1-SNAPSHOT"

organization := "jp.co.guru"

scalaVersion := "2.10.3"

net.virtualvoid.sbt.graph.Plugin.graphSettings

resolvers ++= Seq( 
            "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/",
            "spray repo" at "http://repo.spray.io",
			"orangesignal" at "http://orangesignal.sourceforge.jp/maven2/",
			"Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
			"Big Bee Consultants" at "http://repo.bigbeeconsultants.co.uk/repo"
)

libraryDependencies ++= Seq(
			"ch.qos.logback" % "logback-classic" % "1.0.+" % "test",
			"com.typesafe" % "scalalogging-slf4j_2.10" % "1.0.1",
			"org.scalatest" % "scalatest_2.10" % "2.0" % "test",
			"org.scala-lang" % "scala-swing" % "2.10.3",
			"com.typesafe.akka" %% "akka-actor" % "2.1.0"
)


EclipseKeys.createSrc := EclipseCreateSrc.Default + EclipseCreateSrc.Resource

javacOptions ++= Seq("-encoding", "UTF-8")

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

scalacOptions ++= Seq("-target:jvm-1.6", "-feature")

testOptions in Test += Tests.Argument("-oD")

assemblySettings

jarName in assembly := "MyMine.jar"

test in assembly := {}

mainClass in assembly := Some("jp.co.guru.MyMine.SwingMain")