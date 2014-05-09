package core.db

import scala.pickling._
import binary._
import java.nio.file.{Files, Paths}

object EmployeeTaskTable extends ILinkTable[EmployeeTask] {
  def AddLink(sourceId: Int, targetId: Int) = {
    //DeleteLink(sourceId, targetId)
    val task = EmployeeTask(sourceId, targetId)

    if(list.length > 0) {
      task.id = Option(list.last.id.getOrElse(0) + 1)
    } else {
      task.id = Option(0)
    }

    list = list :+ task
  }

  def write =
    Files.write(Paths.get(fileName), list.pickle.value)

  def read = {
    try {
      list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[EmployeeTask]]
    } catch {
      case e: Exception => write
    }
  }
}
