package core.db

import core.models._
import java.nio.file.{Paths, Files}
import scala.pickling._
import binary._

object TaskTable extends IEntityTable[Task] {
  def write =
    Files.write(Paths.get(fileName), list.pickle.value)

  def read = {
    try {
      list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[Task]]
    } catch {
      case e: Exception => write
    }
  }
}
