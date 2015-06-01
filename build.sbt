name := "db-sample"

version := "0.1"

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
    "org.scalafx"            %% "scalafx"         % "8.0.40-R8",
    "org.scala-lang.modules" %% "scala-pickling"  % "0.10.0",
    "com.novocode"            % "junit-interface" % "0.11" % "test"
)

resolvers ++= Seq(
  "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

// Run in separate VM, so there are no issues with double initialization of JavaFX
fork := true

fork in Test := true

scalacOptions ++= Seq("-feature", "-deprecation")
