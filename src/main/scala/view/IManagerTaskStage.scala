package view

import core.db._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Insets
import scalafx.scene.control.cell.TextFieldTableCell
import scalafx.scene.Scene
import scalafx.scene.control.TableColumn._
import scalafx.scene.layout._
import scalafx.scene.control._
import scalafx.scene.text.Font
import scalafx.util.converter._

class IManagerTaskStage extends VStage {

  ManagerTaskTable.read

  val managerTaskTableModel = new ObservableBuffer[ManagerTask]
  managerTaskTableModel ++= ManagerTaskTable.list

  title = "Scala db Sample"
  val label = new Label("Manager Task ILink Table") {
    font = Font("Arial", 20)
  }

  val emptyLabel = new Label(" ") {
    font = Font("Arial", 20)
  }

  val emptyLabel2 = new Label(" ") {
    font = Font("Arial", 18)
  }

  val table = new TableView[ManagerTask](managerTaskTableModel) {
    columns ++= List(
      new TableColumn[ManagerTask, String] {
        text = "Source Id"
        cellValueFactory = { _.value.vSourceId }
        cellFactory = (_: TableColumn[ManagerTask, String]) ⇒ new TextFieldTableCell[ManagerTask, String] (new DefaultStringConverter)
        prefWidth = 340
      },
      new TableColumn[ManagerTask, String] {
        text = "Target Id"
        cellValueFactory = { _.value.vTargetId }
        cellFactory = (_: TableColumn[ManagerTask, String]) ⇒ new TextFieldTableCell[ManagerTask, String] (new DefaultStringConverter)
        prefWidth = 340
      }
    )
    //editable = true
  }

  val vbox = new VBox {
    children = List(label, table, emptyLabel, emptyLabel2)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
    opacity = 0d
  }

  scene = new Scene {
    content = vbox
  }

  def refreshTableView = {
    ManagerTaskTable.read

    managerTaskTableModel.clear()
    managerTaskTableModel ++= ManagerTaskTable.list
  }
}

object IManagerTaskStage {
  def apply() = new IManagerTaskStage()
}
