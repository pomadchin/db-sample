name := "db-sample"

version := "0.6.0-SNAPSHOT"

scalaVersion := "3.7.2"

libraryDependencies ++= Seq(
  "org.scalafx" %% "scalafx" % "23.0.1-R34",
  "com.lihaoyi" %% "upickle" % "4.2.1"
)

testOptions += Tests.Argument(jupiterTestFramework, "+q", "-v", "--display-mode=tree")
libraryDependencies += "com.github.sbt.junit" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => macClassifier
  case n if n.startsWith("Windows") => "win"
  case _                            => throw new Exception("Unknown platform!")
}

// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map(m => "org.openjfx" % s"javafx-$m" % "23.0.2" classifier osName)

// Run in separate VM, so there are no issues with double initialization of JavaFX
fork := true

Test / fork := true

scalacOptions ++= Seq("-feature", "-deprecation")

def macClassifier: String = {
  val line = scala.io.Source.fromString(scala.sys.process.Process("uname -sm").!!).getLines().next()
  if(line.split(" ").last.toLowerCase.replaceAll("\\s", "") == "arm64") "mac-aarch64" else "mac"
}
