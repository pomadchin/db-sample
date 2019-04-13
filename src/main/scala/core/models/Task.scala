package core.models

import core.db._

import upickle.default.{ReadWriter => RW, macroRW}
import scalafx.beans.property.StringProperty

case class Task(name: String) extends Identifiable {
  lazy val vName = new StringProperty(this, "name", name)
}

object Task {
  implicit val rw: RW[Task] = macroRW
}
