name := "db-sample"

version := "0.4.1-SNAPSHOT"

scalaVersion := "3.1.0"

libraryDependencies ++= Seq(
  "org.scalafx"   %% "scalafx"         % "17.0.1-R26",
  "com.lihaoyi"   %% "upickle"         % "1.4.3",
  "com.github.sbt" % "junit-interface" % "0.13.3" % Test
)

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _                            => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map(m => "org.openjfx" % s"javafx-$m" % "17" classifier osName)

// Run in separate VM, so there are no issues with double initialization of JavaFX
fork := true

Test / fork := true

scalacOptions ++= Seq("-feature", "-deprecation")
