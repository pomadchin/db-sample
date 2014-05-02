package core.db

import scalafx.beans.property.StringProperty

case class EmployeeTask(sourceId: Int, targetId: Int) extends ILink {
  val vSourceId = new StringProperty(this, "sourceId", sourceId.toString)
  val vTargetId = new StringProperty(this, "targetId", targetId.toString)
}
