package core.db

import core.models._
import java.nio.file.{Paths, Files}
import scala.pickling._
import binary._

object ManagerTable extends IEntityTable[Manager] {
  def write =
    Files.write(Paths.get(fileName), list.pickle.value)

  def read =
    list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[Manager]]
}

