package core.db

import scala.pickling._
import binary._
import java.nio.file.{Files, Paths}
import java.io.File

object EmployeeTaskTable extends ILinkTable[EmployeeTask] {
  def AddLink(sourceId: Int, targetId: Int) = {
    DeleteLink(sourceId, targetId)
    val task = EmployeeTask(sourceId, targetId)

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
          case e: Exception => println("Can't create tmp dir.")
        }

        if(f) write
      }
    }
  }

  def read = {
    try {
      list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[EmployeeTask]]
    } catch {
      case e: Exception => write
    }
  }
}
