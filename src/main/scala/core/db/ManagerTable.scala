package core.db

import core.models._
import upickle.default._

import java.nio.file.{Paths, Files}
import java.io.File
import scala.util.{Failure, Success, Try}
import scalafx.collections.ObservableBuffer

object ManagerTable extends IEntityTable[Manager] {
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

  def read = {
    Try {
      list = readBinary[List[Manager]](Files.readAllBytes(Paths.get(fileName)))
    } match {
      case Success(v) ⇒ { }
      case Failure(e) ⇒ write
    }
  }

  def find(pair: (Option[String], Option[String]) = (None, None)): List[Manager] = {
    pair match {
      case (Some(name), Some(position)) ⇒ list.filter(m ⇒ m.fio == name && m.position == position)
      case (Some(name), None)           ⇒ list.filter(m ⇒ m.fio == name)
      case (None, Some(position))       ⇒ list.filter(m ⇒ m.position == position)
      case (None, None)                 ⇒ List[Manager]()
    }
  }

  def sumSalary(index: Int, managerTableModel: ObservableBuffer[Manager]): Double = {

    val mi = managerTableModel.get(index).id.getOrElse(0)

    val managerTaskIds = ManagerTaskTable.GetTargetIds(mi)
    val employeeIds =
      EmployeeTaskTable.list.filter(t ⇒ (List(t.targetId) intersect managerTaskIds).nonEmpty).map(_.sourceId)

    val employees = EmployeeTable.list.filter(e ⇒ (List(e.id.getOrElse(0)) intersect employeeIds).nonEmpty)

    employees.foldLeft(0.0)((acc, e) ⇒ acc + e.salary)
  }

  def DeleteCascade(id: Int) = {
    ManagerTable.Delete(id)

    // cascade remove
    ManagerTaskTable.GetTargetIds(id).foreach(t ⇒ ManagerTaskTable.DeleteLink(id, t))
  }

}
