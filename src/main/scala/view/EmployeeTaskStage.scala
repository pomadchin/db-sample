package view

import core.db._
import core.models._
import scalafx.application.JFXApp.PrimaryStage
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
import scalafx.scene.shape.Rectangle
import scalafx.stage.Stage

class EmployeeTaskStage(id: Option[Int] = None) extends Stage {
  val employeeTable     = EmployeeTable
  val employeeTaskTable = EmployeeTaskTable
  val taskTable         = TaskTable

  employeeTable.read
  employeeTaskTable.read
  taskTable.read

  val taskIds = employeeTaskTable.list.filter(_.sourceId == id.getOrElse(0)).map(_.targetId)

  val taskTableModel = new ObservableBuffer[Task]
  taskTableModel ++= taskTable.list.filter(t => (List(t.id.getOrElse(0)) intersect taskIds).length > 0)

  title = "Scala db Sample"
  val label = new Label("EmployeeTaskTable") {
    font = Font("Arial", 20)
  }

  val table = new TableView[Task](taskTableModel) {
    columns ++= List(
      new TableColumn[Task, String] {
        text = "name"
        cellValueFactory = { _.value.vName }
        cellFactory = _ => new TextFieldTableCell[Task, String] (new DefaultStringConverter())
        onEditCommit = (evt: CellEditEvent[Task, String]) => {
          val employee = evt.rowValue
          val newLastFioVal = evt.newValue
          // Update current person data set
          println(employee.toString + " " + newLastFioVal)
          println(employee.id.getOrElse(0).toString)
        }
        prefWidth = 180
      },
      new TableColumn[Task, Boolean] {
        text = "action"
        cellValueFactory = { e => ObjectProperty[Boolean](e.value != null) }
        cellFactory = _ => new TableCell[Task, Boolean] {
          alignment = Pos.CENTER
          item.onChange((_, _, p) =>
            if(p) {
              graphic = new HBox {
                content = List(
                  new Button("Delete") {
                    onAction = (ae: ActionEvent) => {
                      println(1)
                    }
                  },
                  new Button("Add Tasks") {
                    onAction = (ae: ActionEvent) => {

                      val tasksStage = new Stage {
                        scene = new Scene {
                          content = Set(new Rectangle {
                            x = 25
                            y = 40
                            width = 100
                            height = 100
                          })
                        }
                      }

                      tasksStage.show
                    }
                  }
                )
                spacing = 10
                alignment = Pos.CENTER
                //padding = Insets(10, 10, 10, 10)
              }
            }
          )
        }
        prefWidth = 180
      }
    )
    //editable = true
  }

  val fioTextField = new TextField {
    promptText = "fio"
    maxWidth = 180
  }

  val salaryTextField = new TextField {
    promptText = "salary"
    maxWidth = 100
  }

  val addButton = new Button("Add") {
    onAction = (_:ActionEvent) => {
      val employee = Employee(fioTextField.getText, salaryTextField.getText.toDouble)
      println(employee.toString)

      employeeTable.Add(employee)
      refreshTableView
    }
  }

  val hbox = new HBox {
    content = List(fioTextField, salaryTextField, addButton)
    spacing = 10
  }

  scene = new Scene {
    content = new VBox {
      content = List(label, table, hbox)
      spacing = 10
      padding = Insets(10, 10, 10, 10)
    }
  }

  def refreshTableView = {
    println(1)
  }
}
