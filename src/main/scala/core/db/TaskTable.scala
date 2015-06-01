package core.db

import core.models._
import java.nio.file.{Paths, Files}
import scala.pickling.Defaults._
import scala.pickling._
import binary._
import java.io.File

object TaskTable extends IEntityTable[Task] {
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

  def find(name: Option[String] = None): List[Task] = {
    name match {
      case Some(name) => list.filter(t => t.name == name)
      case None       => List[Task]()
    }
  }

  def read = {
    try {
      list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[Task]]
    } catch {
      case e: Exception => write
    }
  }

  def DeleteCascade(id: Int) = {
    TaskTable.Delete(id)

    // cascade remove
    val employeeTasks = EmployeeTaskTable.list.filter(_.targetId == id)
    val managerTasks = ManagerTaskTable.list.filter(_.targetId == id)

    managerTasks.foreach(t => ManagerTaskTable.DeleteLink(t.sourceId, t.targetId))
    employeeTasks.foreach(t => EmployeeTaskTable.DeleteLink(t.sourceId, t.targetId))
  }
}
