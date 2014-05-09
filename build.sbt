name := "db-sample"

version := "0.1"

scalaVersion := "2.10.2"

assemblySettings

libraryDependencies ++= Seq(
    "org.scalafx"    %% "scalafx"         % "1.0.0-R8",
    "org.scala-lang" %% "scala-pickling"  % "0.8.0",
    "com.novocode"    % "junit-interface" % "0.10" % "test"
)

resolvers ++= Seq(
  "Sonatype OSS Releases"  at "http://oss.sonatype.org/content/repositories/releases/",
  "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
)

unmanagedResourceDirectories in Compile <+= baseDirectory { _/"src/main/scala"}

// Add dependency on JavaFX library (only for Java 7)
unmanagedJars in Compile += Attributed.blank(file(scala.util.Properties.javaHome) / "/lib/jfxrt.jar")

// Run in separate VM, so there are no issues with double initialization of JavaFX
fork := true

fork in Test := true

scalacOptions ++= Seq("-feature", "-deprecation")
