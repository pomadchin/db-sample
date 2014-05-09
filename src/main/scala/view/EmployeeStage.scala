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

class EmployeeStage extends VStage {
  val employeeTable     = EmployeeTable
  val employeeTaskTable = EmployeeTaskTable
  val taskTable         = TaskTable

  employeeTable.read
  employeeTaskTable.read
  taskTable.read

  val employeeTableModel = new ObservableBuffer[Employee]
  employeeTableModel ++= employeeTable.list

  title = "Scala db Sample"
  val label = new Label("Employee Table") {
    font = Font("Arial", 20)
  }

  val table = new TableView[Employee](employeeTableModel) {
    columns ++= List(
      new TableColumn[Employee, String] {
        text = "Id"
        cellValueFactory = { c => new StringProperty(this, "id", c.value.id.getOrElse(0).toString) }
        cellFactory = _ => new TextFieldTableCell[Employee, String] { alignment = Pos.CENTER }; (new DefaultStringConverter)
        prefWidth = 40
      },
      new TableColumn[Employee, String] {
        text = "Name"
        cellValueFactory = { _.value.vFio }
        cellFactory = _ => new TextFieldTableCell[Employee, String] (new DefaultStringConverter)
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
        text = "Salary"
        cellValueFactory = { _.value.vSalary }
        cellFactory = _ => new TextFieldTableCell[Employee, String] (new DefaultStringConverter)
        onEditCommit = (evt: CellEditEvent[Employee, String]) => {
          val employee = evt.rowValue
          val newLastSalaryVal = evt.newValue
          // Update current person data set
          println(employee.toString + " " + newLastSalaryVal)
          println(employee.id.getOrElse(0).toString)
        }
        prefWidth = 180
      },
      new TableColumn[Employee, Boolean] {
        text = "Action"
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
                        employeeTasks.foreach(t => {
                          employeeTaskTable.DeleteLink(ei, t.id.getOrElse(0))
                          taskTable.Delete(t.id.getOrElse(0))
                        })

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
    promptText = "Name"
    maxWidth = 180
  }

  val salaryTextField = new TextField {
    promptText = "Salary"
    maxWidth = 100
  }

  val addButton = new Button("Add") {
    onAction = (_:ActionEvent) => {
      val salary = if(isNumeric(salaryTextField.getText)) salaryTextField.getText.toDouble else 0.0
      val employee = Employee(fioTextField.getText, salary)

      employeeTable.Add(employee)
      refreshTableView
    }
  }

  val hbox = new HBox {
    content = List(fioTextField, salaryTextField, addButton)
    spacing = 10
  }

  val vbox = new VBox {
    content = List(label, table, hbox)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
    opacity = 0.0
  }

  scene = new Scene {
    content = vbox
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

  def isNumeric(str: String): Boolean = str.matches("""\d+(\.\d*)?""")

}
