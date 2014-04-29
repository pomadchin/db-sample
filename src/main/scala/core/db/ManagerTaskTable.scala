package core.db

object ManagerTaskTable extends ILinkTable[ManagerTask] {
  def AddLink(sourceId: Int, targetId: Int) = list :+ ManagerTask(sourceId, targetId)
}
