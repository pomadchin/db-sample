package core.db

import core.models._
import java.nio.file.{Paths, Files}
import scala.pickling._
import binary._
import scalafx.collections.ObservableBuffer

object TaskTable extends IEntityTable[Task] {
  def write =
    Files.write(Paths.get(fileName), list.pickle.value)

  def read =
    list = BinaryPickle(Files.readAllBytes(Paths.get(fileName))).unpickle[List[Task]]
}
