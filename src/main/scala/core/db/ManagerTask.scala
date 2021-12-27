package core.db

import upickle.default.{macroRW, ReadWriter as RW}
import scalafx.beans.property.StringProperty

case class ManagerTask(sourceId: Int, targetId: Int) extends ILink:
  lazy val vSourceId = new StringProperty(this, "sourceId", sourceId.toString)
  lazy val vTargetId = new StringProperty(this, "targetId", targetId.toString)

object ManagerTask:
  implicit val rw: RW[ManagerTask] = macroRW
