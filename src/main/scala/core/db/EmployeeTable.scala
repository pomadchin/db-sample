package core.db

import core.models._
import java.nio.file.{Paths, Files}
import scala.pickling.Defaults._
import scala.pickling._
import binary._
import java.io.File

object EmployeeTable extends IEntityTable[Employee] {
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
      list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[Employee]]
    } catch {
      case e: Exception => write
    }
  }

  def find(pair: (Option[String], Option[Double]) = (None, None)): List[Employee] = {
    pair match {
      case (Some(name), Some(salary)) => list.filter(e => e.fio == name && e.salary == salary)
      case (Some(name), None)         => list.filter(e => e.fio == name)
      case (None, Some(salary))       => list.filter(e => e.salary == salary)
      case (None, None)               => List[Employee]()
    }
  }

  def DeleteCascade(id: Int) = {
    EmployeeTable.Delete(id)

    // cascade remove
    EmployeeTaskTable.GetTargetIds(id).foreach(t => EmployeeTaskTable.DeleteLink(id, t))
  }

}
