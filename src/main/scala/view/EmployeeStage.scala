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
import scalafx.Includes._
import scalafx.beans.property._
import scalafx.Includes._
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.stage.Stage

class EmployeeStage extends PrimaryStage {
  val employeeTable     = EmployeeTable
  val employeeTaskTable = EmployeeTaskTable
  val taskTable         = TaskTable

  employeeTable.read
  employeeTaskTable.read
  taskTable.read

  val employeeTableModel = new ObservableBuffer[Employee]
  employeeTableModel ++= employeeTable.list

  title = "Scala db Sample"
  val label = new Label("EmployeeTable") {
    font = Font("Arial", 20)
  }

  val table = new TableView[Employee](employeeTableModel) {
    columns ++= List(
      new TableColumn[Employee, String] {
        text = "fio"
        cellValueFactory = { _.value.vFio }
        cellFactory = _ => new TextFieldTableCell[Employee, String] (new DefaultStringConverter())
        onEditCommit = (evt: CellEditEvent[Employee, String]) => {
          val employee = evt.rowValue
          val newLastFioVal = evt.newValue
          // Update current person data set
          println(employee.toString + " " + newLastFioVal)
          println(employee.id.getOrElse(0).toString)
        }
        prefWidth = 180
      },
      new TableColumn[Employee, String] {
        text = "salary"
        cellValueFactory = { _.value.vSalary }
        cellFactory = _ => new TextFieldTableCell[Employee, String] (new DefaultStringConverter)
        onEditCommit = (evt: CellEditEvent[Employee, String]) => {
          val employee = evt.rowValue
          val newLastSalaryVal = evt.newValue
          // Update current person data set
          println(employee.toString + " " + newLastSalaryVal)
          println(employee.id.getOrElse(0).toString)
        }
        prefWidth = 100
      },
      new TableColumn[Employee, Boolean] {
        text = "action"
        cellValueFactory = { e => ObjectProperty[Boolean](e.value != null) }
        cellFactory = _ => new TableCell[Employee, Boolean] {
          alignment = Pos.CENTER
          item.onChange((_, _, p) =>
            if(p) {
              graphic = new HBox {
                content = List(
                  new Button("Delete") {
                    onAction = (ae: ActionEvent) => {
                      val ei = employeeTableModel.get(index.value).id.getOrElse(0)

                      if(index.value < employeeTableModel.length) {
                        employeeTable.Delete(ei)

                        // cascade remove
                        val employeeTasks = employeeTaskTable.list.filter(_.sourceId == ei)
                        val employeeTaskIds = employeeTasks.map(_.targetId)
                        val tasks = taskTable.list.filter(t => (List(t.id.getOrElse(0)) intersect employeeTaskIds).length > 0)

                        tasks.foreach(t => taskTable.Delete(t.id.getOrElse(0)))
                        employeeTasks.foreach(t => taskTable.Delete(t.id.getOrElse(0)))

                        refreshTableView
                      }
                    }
                  },
                  new Button("Add Tasks") {
                    onAction = (ae: ActionEvent) => {
                      val tasksStage = new EmployeeTaskStage(employeeTableModel.get(index.value).id)
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
    employeeTable.write
    taskTable.write
    employeeTaskTable.write
    employeeTable.read
    taskTable.read
    employeeTaskTable.read

    employeeTableModel.clear
    employeeTableModel ++= employeeTable.list

    fioTextField.clear
    salaryTextField.clear
  }
}
