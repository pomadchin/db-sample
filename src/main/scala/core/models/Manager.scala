package core.models

import core.db._

import upickle.default.{ReadWriter => RW, macroRW}
import scalafx.beans.property.StringProperty

case class Manager(fio: String, position: String) extends Identifiable {
  lazy val vFio      = new StringProperty(this, "fio", fio)
  lazy val vPosition = new StringProperty(this, "position", position)
}

object Manager {
  implicit val rw: RW[Manager] = macroRW
}
