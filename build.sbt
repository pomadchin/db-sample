name := "db-sample"

version := "0.2.2"

scalaVersion := "2.11.12"

crossScalaVersions := Seq("2.12.8", "2.11.12")

libraryDependencies ++= Seq(
  "org.scalafx"            %% "scalafx"         % "8.0.181-R13",
  "org.scala-lang.modules" %% "scala-pickling"  % "0.10.1",
  "com.novocode"            % "junit-interface" % "0.11" % Test
)

resolvers ++= Seq(
  "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

// Run in separate VM, so there are no issues with double initialization of JavaFX
fork := true

fork in Test := true

scalacOptions ++= Seq("-feature", "-deprecation")
