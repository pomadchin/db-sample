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
import scalafx.stage.Stage

class IManagerTaskStage extends VStage {
  val managerTaskTable = ManagerTaskTable

  managerTaskTable.read

  val managerTaskTableModel = new ObservableBuffer[ManagerTask]
  managerTaskTableModel ++= managerTaskTable.list

  title = "Scala db Sample"
  val label = new Label("Manager Task ILink Table") {
    font = Font("Arial", 20)
  }

  val emptyLabel = new Label(" ") {
    font = Font("Arial", 20)
  }

  val table = new TableView[ManagerTask](managerTaskTableModel) {
    columns ++= List(
      new TableColumn[ManagerTask, String] {
        text = "Source Id"
        cellValueFactory = { _.value.vSourceId }
        cellFactory = _ => new TextFieldTableCell[ManagerTask, String] (new DefaultStringConverter)
        prefWidth = 290
      },
      new TableColumn[ManagerTask, String] {
        text = "Target Id"
        cellValueFactory = { _.value.vTargetId }
        cellFactory = _ => new TextFieldTableCell[ManagerTask, String] (new DefaultStringConverter)
        prefWidth = 290
      }
    )
    //editable = true
  }

  val vbox = new VBox {
    content = List(label, table, emptyLabel)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
    opacity = 0.0
  }

  scene = new Scene {
    content = vbox
  }

  def refreshTableView = {
    managerTaskTable.read

    managerTaskTableModel.clear
    managerTaskTableModel ++= managerTaskTable.list
  }
}
