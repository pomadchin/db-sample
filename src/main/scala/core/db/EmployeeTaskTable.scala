package core.db

import upickle.default._

import java.nio.file.{Files, Paths}
import java.io.File
import scala.util.{Failure, Success, Try}

object EmployeeTaskTable extends ILinkTable[EmployeeTask] {
  def AddLink(sourceId: Int, targetId: Int) = {
    DeleteLink(sourceId, targetId)
    val task = EmployeeTask(sourceId, targetId)

    list = list :+ task
  }

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
      list = readBinary[List[EmployeeTask]](Files.readAllBytes(Paths.get(fileName)))
    } match {
      case Success(v) ⇒ { }
      case Failure(e) ⇒ write
    }
  }
}
