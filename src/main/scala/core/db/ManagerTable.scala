package core.db

import core.models._
import java.nio.file.{Paths, Files}
import scala.pickling._
import binary._
import java.io.File
import scalafx.collections.ObservableBuffer

object ManagerTable extends IEntityTable[Manager] {
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
      list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[Manager]]
    } catch {
      case e: Exception => write
    }
  }

  def find(pair: (Option[String], Option[String]) = (None, None)): List[Manager] = {
    pair match {
      case (Some(name), Some(position)) => list.filter(m => m.fio == name && m.position == position)
      case (Some(name), None)           => list.filter(m => m.fio == name)
      case (None, Some(position))       => list.filter(m => m.position == position)
      case (None, None)                 => List[Manager]()
    }
  }

  def sumSalary(index: Int, managerTableModel: ObservableBuffer[Manager]): Double = {

    val mi = managerTableModel.get(index).id.getOrElse(0)

    val managerTaskIds = ManagerTaskTable.GetTargetIds(mi)
    val employeeIds =
      EmployeeTaskTable.list.filter(t => (List(t.targetId) intersect managerTaskIds).length > 0).map(_.sourceId)

    val employees = EmployeeTable.list.filter(e => (List(e.id.getOrElse(0)) intersect employeeIds).length > 0)

    employees.foldLeft(0.0)((acc, e) => acc + e.salary)
  }

  def DeleteCascade(id: Int) = {
    ManagerTable.Delete(id)

    // cascade remove
    ManagerTaskTable.GetTargetIds(id).foreach(t => ManagerTaskTable.DeleteLink(id, t))
  }

}
