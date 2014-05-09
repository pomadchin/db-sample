package view

import core.db._
import core.models._
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Pos, Insets}
import scalafx.scene.control.cell.TextFieldTableCell
import scalafx.scene.Scene
import scalafx.scene.control.TableColumn._
import scalafx.scene.layout._
import scalafx.scene.control._
import scalafx.scene.text.Font
import scalafx.util.converter._
import scalafx.beans.property._
import scalafx.Includes._
import scalafx.stage.Stage

class IEmployeeTaskStage extends VStage {
  val employeeTaskTable = EmployeeTaskTable

  employeeTaskTable.read

  val employeeTaskTableModel = new ObservableBuffer[EmployeeTask]
  employeeTaskTableModel ++= employeeTaskTable.list

  title = "Scala db Sample"
  val label = new Label("Employee Task ILink Table") {
    font = Font("Arial", 20)
  }

  val emptyLabel = new Label(" ") {
    font = Font("Arial", 20)
  }

  val table = new TableView[EmployeeTask](employeeTaskTableModel) {
    columns ++= List(
      new TableColumn[EmployeeTask, String] {
        text = "Source Id"
        cellValueFactory = { _.value.vSourceId }
        cellFactory = _ => new TextFieldTableCell[EmployeeTask, String] (new DefaultStringConverter)
        prefWidth = 290
      },
      new TableColumn[EmployeeTask, String] {
        text = "Target Id"
        cellValueFactory = { _.value.vTargetId }
        cellFactory = _ => new TextFieldTableCell[EmployeeTask, String] (new DefaultStringConverter)
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
    employeeTaskTable.read

    employeeTaskTableModel.clear
    employeeTaskTableModel ++= employeeTaskTable.list
  }
}
