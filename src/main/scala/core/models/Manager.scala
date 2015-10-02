package core.models

import scalafx.beans.property.StringProperty
import core.db._

case class Manager(fio: String, position: String) extends Identifiable {
  lazy val vFio      = new StringProperty(this, "fio", fio)
  lazy val vPosition = new StringProperty(this, "position", position)
}
