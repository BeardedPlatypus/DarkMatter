name := "DarkMatter"

version := "1.0"

scalaVersion := "2.11.6"

val akkaVersion  = "2.3.9"

// libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3"

// add scala-xml dependency when needed (for Scala 2.11 and newer) in a robust way
// this mechanism supports cross-version publishing
// taken from: http://github.com/scala/scala-module-dependency-sample
libraryDependencies := {
  CrossVersion.partialVersion(scalaVersion.value) match {
    // if scala 2.11+ is used, add dependency on scala-xml module
    case Some((2, scalaMajor)) if scalaMajor >= 11 =>
      libraryDependencies.value ++ Seq(
        "org.scala-lang.modules" %% "scala-xml" % "1.0.3",
        "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3",
        "org.scala-lang.modules" %% "scala-swing" % "1.0.1",
        "com.typesafe.akka" %% "akka-actor" % akkaVersion)
    case _ =>
      // or just libraryDependencies.value if you don't depend on scala-swing
      libraryDependencies.value :+ "org.scala-lang" % "scala-swing" % scalaVersion.value
  }
}

scalacOptions ++= Seq(
      "-deprecation",
      "-unchecked",
      "-optimize")
      
javaOptions += "-Xmx2G"
