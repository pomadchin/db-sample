package core.db

object EmployeeTaskTable extends ILinkTable[EmployeeTask] {
  def AddLink(sourceId: Int, targetId: Int) = list :+ EmployeeTask(sourceId, targetId)
}
