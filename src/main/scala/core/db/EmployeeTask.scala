package core.db

import scalafx.beans.property.StringProperty

case class EmployeeTask(sourceId: Int, targetId: Int) extends ILink {
  lazy val vSourceId = new StringProperty(this, "sourceId", sourceId.toString)
  lazy val vTargetId = new StringProperty(this, "targetId", targetId.toString)
}
