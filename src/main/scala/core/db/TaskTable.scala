package core.db

import core.models._
import upickle.default._

import java.nio.file.{Paths, Files}
import java.io.File
import scala.util.{Failure, Success, Try}

object TaskTable extends IEntityTable[Task] {
  def write: Unit = {
    Try {
      Files.write(Paths.get(fileName), writeBinary(list))
    } match {
      case Success(v) ⇒ { }
      case Failure(e) ⇒ Try {
        new File("tmp/").mkdir
      } match {
        case Success(v) ⇒ write
        case Failure(e) ⇒ println("Can't create tmp dir.")
      }
    }
  }

  def find(name: Option[String] = None): List[Task] = {
    name match {
      case Some(name) ⇒ list.filter(t ⇒ t.name == name)
      case None       ⇒ List[Task]()
    }
  }

  def read = {
    Try {
      list = readBinary[List[Task]](Files.readAllBytes(Paths.get(fileName)))
    } match {
      case Success(v) ⇒ { }
      case Failure(e) ⇒ write
    }
  }

  def DeleteCascade(id: Int) = {
    TaskTable.Delete(id)

    // cascade remove
    val employeeTasks = EmployeeTaskTable.list.filter(_.targetId == id)
    val managerTasks = ManagerTaskTable.list.filter(_.targetId == id)

    managerTasks.foreach(t ⇒ ManagerTaskTable.DeleteLink(t.sourceId, t.targetId))
    employeeTasks.foreach(t ⇒ EmployeeTaskTable.DeleteLink(t.sourceId, t.targetId))
  }
}
