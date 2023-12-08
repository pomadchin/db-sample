name := "db-sample"

version := "0.5.0-SNAPSHOT"

scalaVersion := "3.3.1"

libraryDependencies ++= Seq(
  "org.scalafx"      %% "scalafx"           % "21.0.0-R32",
  "com.lihaoyi"      %% "upickle"           % "3.1.3",
  "org.junit.jupiter" % "junit-jupiter-api" % "5.10.1" % Test,
  "com.github.sbt"    % "junit-interface"   % "0.13.3" % Test
)

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => macClassifier
  case n if n.startsWith("Windows") => "win"
  case _                            => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map(m => "org.openjfx" % s"javafx-$m" % "21.0.1" classifier osName)

// Run in separate VM, so there are no issues with double initialization of JavaFX
fork := true

Test / fork := true

scalacOptions ++= Seq("-feature", "-deprecation")

def macClassifier: String = {
  val line = scala.io.Source.fromString(scala.sys.process.Process("uname -sm").!!).getLines().next()
  if(line.split(" ").last.toLowerCase.replaceAll("\\s", "") == "arm64") "mac-aarch64" else "mac"
}
