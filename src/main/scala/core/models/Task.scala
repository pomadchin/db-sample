package core.models

import scalafx.beans.property.StringProperty
import core.db._

case class Task(name: String) extends Identifiable {
  lazy val vName = new StringProperty(this, "name", name)
}
