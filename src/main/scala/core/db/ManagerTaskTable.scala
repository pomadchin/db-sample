package core.db

import scala.pickling.Defaults._
import scala.pickling._
import binary._
import java.nio.file.{Files, Paths}
import java.io.File

object ManagerTaskTable extends ILinkTable[ManagerTask] {
  def AddLink(sourceId: Int, targetId: Int) = {
    DeleteLink(sourceId, targetId)
    val task = ManagerTask(sourceId, targetId)

    list = list :+ task
  }

  def write: Unit = {
    try {
      Files.write(Paths.get(fileName), list.pickle.value)
    } catch {
      case e: Exception => {
        var f = false
        try {
          f = new File("tmp/").mkdir
        } catch {
          case e: Exception => println("Can't create tmp/ dir.")
        }

        if(f) write
      }
    }
  }

  def read = {
    try {
      list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[ManagerTask]]
    } catch {
      case e: Exception => write
    }
  }
}
