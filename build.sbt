name := "db-sample"

version := "0.1"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-swing" % scalaVersion.value,
    "org.scala-lang" %% "scala-pickling" % "0.8.0"
)

resolvers ++= Seq(
  "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)
