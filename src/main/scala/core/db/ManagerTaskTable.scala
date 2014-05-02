package core.db

import scala.pickling._
import binary._
import java.nio.file.{Files, Paths}
import scalafx.collections.ObservableBuffer

object ManagerTaskTable extends ILinkTable[ManagerTask] {
  def AddLink(sourceId: Int, targetId: Int) = {
    DeleteLink(sourceId, targetId)
    list :+ ManagerTask(sourceId, targetId)
  }

  def write =
    Files.write(Paths.get(fileName), list.pickle.value)

  def read =
    list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[ManagerTask]]
}
