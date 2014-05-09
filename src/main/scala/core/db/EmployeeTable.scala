package core.db

import core.models._
import java.nio.file.{Paths, Files}
import scala.pickling._
import binary._

object EmployeeTable extends IEntityTable[Employee] {
  def write =
    Files.write(Paths.get(fileName), list.pickle.value)

  def read = {
    try {
      list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[Employee]]
    } catch {
      case e: Exception => write
    }

  }
}
