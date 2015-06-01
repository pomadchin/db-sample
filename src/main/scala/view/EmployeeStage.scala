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

  EmployeeTable.read
  EmployeeTaskTable.read
  TaskTable.read

  val employeeTableModel = new ObservableBuffer[Employee]
  employeeTableModel ++= EmployeeTable.list

  title = "Scala db Sample"
  val label = new Label("Employee Table") {
    font = Font("Arial", 20)
  }

  val table = new TableView[Employee](employeeTableModel) {
    columns ++= List(
      new TableColumn[Employee, String] {
        text = "Id"
        cellValueFactory = { c => new StringProperty(this, "id", c.value.id.getOrElse(0).toString) }
        cellFactory = _ => new TextFieldTableCell[Employee, String] { alignment = Pos.Center }; new DefaultStringConverter
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
          alignment = Pos.Center
          item.onChange((_, _, p) =>
            if(p) {
              graphic = new HBox {
                children = List(
                  new Button("Delete") {
                    onAction = (ae: ActionEvent) => {
                      val ei = employeeTableModel.get(index.value).id.getOrElse(0)

                      if(index.value < employeeTableModel.length) {
                        EmployeeTable.DeleteCascade(ei)
                        refreshTableView
                      }
                    }
                  },
                  new Button("Add Tasks") {
                    onAction = (ae: ActionEvent) => {
                      val tasksStage = new EmployeeTaskStage(employeeTableModel.get(index.value).id)
                      tasksStage.show()
                    }
                  }
                )
                spacing = 10
                alignment = Pos.Center
                //padding = Insets(10, 10, 10, 10)
              }
            }
          )
        }
        prefWidth = 280
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
    maxWidth = 180
  }

  val addButton = new Button("Add") {
    onAction = (_:ActionEvent) => {
      val salary = if(isNumeric(salaryTextField.getText)) salaryTextField.getText.toDouble else 0.0
      val employee = Employee(fioTextField.getText, salary)

      EmployeeTable.Add(employee)
      refreshTableView
    }
  }

  val fioSearchTextField = new TextField {
    promptText = "Name"
    maxWidth = 180
  }

  val salarySearchTextField = new TextField {
    promptText = "Salary"
    maxWidth = 180
  }

  val searchButton = new Button("Search") {
    onAction = (_:ActionEvent) => {
      val salary = if(isNumeric(salarySearchTextField.getText)) Some(salarySearchTextField.getText.toDouble) else None
      val name   = if(fioSearchTextField.getText.length > 0) Some(fioSearchTextField.getText) else None
      val employeeList = EmployeeTable.find(name -> salary)

      employeeTableModel.clear()
      employeeTableModel ++= employeeList
    }
  }

  val refreshButton = new Button("Refresh") {
    onAction = (_:ActionEvent) => refreshTableView
  }

  val hbox = new HBox {
    children = List(fioTextField, salaryTextField, addButton)
    spacing = 10
  }

  val hSearchBox = new HBox {
    children = List(fioSearchTextField, salarySearchTextField, searchButton, refreshButton)
    spacing = 10
  }

  val vbox = new VBox {
    children = List(label, table, hbox, hSearchBox)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
    opacity = 0.0
  }

  scene = new Scene {
    content = vbox
  }

  def refreshTableView = {
    EmployeeTable.write
    TaskTable.write
    EmployeeTaskTable.write
    EmployeeTable.read
    TaskTable.read
    EmployeeTaskTable.read

    employeeTableModel.clear()
    employeeTableModel ++= EmployeeTable.list

    fioTextField.clear()
    salaryTextField.clear()
    fioSearchTextField.clear()
    salarySearchTextField.clear()
  }
}
