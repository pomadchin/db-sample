package core.db

import core.models._
import upickle.default._

import java.nio.file.{Paths, Files}
import java.io.File
import scala.util.{Try, Success, Failure}

object EmployeeTable extends IEntityTable[Employee] {
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
      list = readBinary[List[Employee]](Files.readAllBytes(Paths.get(fileName)))
    } match {
      case Success(v) ⇒ { }
      case Failure(e) ⇒ write
    }
  }

  def find(pair: (Option[String], Option[Double]) = (None, None)): List[Employee] = {
    pair match {
      case (Some(name), Some(salary)) ⇒ list.filter(e ⇒ e.fio == name && e.salary == salary)
      case (Some(name), None)         ⇒ list.filter(e ⇒ e.fio == name)
      case (None, Some(salary))       ⇒ list.filter(e ⇒ e.salary == salary)
      case (None, None)               ⇒ List[Employee]()
    }
  }

  def DeleteCascade(id: Int) = {
    EmployeeTable.Delete(id)

    // cascade remove
    EmployeeTaskTable.GetTargetIds(id).foreach(t ⇒ EmployeeTaskTable.DeleteLink(id, t))
  }
}
