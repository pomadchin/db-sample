package core.db

import scala.pickling._
import binary._
import java.nio.file.{Files, Paths}

object EmployeeTaskTable extends ILinkTable[EmployeeTask] {
  def AddLink(sourceId: Int, targetId: Int) = list :+ EmployeeTask(sourceId, targetId)

  def write =
    Files.write(Paths.get(fileName), list.pickle.value)

  def read =
    list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[EmployeeTask]]
}
