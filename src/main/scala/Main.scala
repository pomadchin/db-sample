import core.db._
import core.models._

import scalafx.application.JFXApp
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


object Main extends JFXApp {

  val employeeTable = EmployeeTable
  employeeTable.read

  val employeeTableModel = new ObservableBuffer[Employee]
  employeeTableModel ++= employeeTable.list


  stage = new PrimaryStage {
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
          prefWidth = 180
        },
        new TableColumn[Employee, Boolean] {
          text = "action"
          cellValueFactory = { e => ObjectProperty[Boolean](e.value != null) }
          cellFactory = _ => new TableCell[Employee, Boolean] {
            alignment = Pos.CENTER
            item.onChange((_, _, p) =>
              if(p) {
                graphic = new Button("Delete") {
                  onAction = (ae: ActionEvent) => {
                    println("empty: " + empty.value)
                    println("index: " + index.value)

                    employeeTable.DeleteByListId(index.value)
                    employeeTable.write
                    employeeTable.read

                    employeeTableModel.clear
                    employeeTableModel ++= employeeTable.list

                    fioTextField.clear
                    salaryTextField.clear
                  }
                }
              }
            )

            //println("empty_s: " + empty.value)

            //println(tableRow)
            //println(tableView)
            //if(empty.value) {


            /*  graphic = new Button("Delete") {
                onAction = (ae: ActionEvent) => {
                  println("empty: " + empty.value)
                  println("index: " + index.value)

                  employeeTable.DeleteByListId(index.value)
                  employeeTable.write
                  employeeTable.read

                  employeeTableModel.clear
                  employeeTableModel ++= employeeTable.list

                  fioTextField.clear
                  salaryTextField.clear
                }
              }*/
            //}
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
      maxWidth = 140
    }

    val addButton = new Button("Add") {
      onAction = (_:ActionEvent) => {
        val employee = Employee(fioTextField.getText, salaryTextField.getText.toDouble)
        println(employee.toString)

        employeeTable.Add(employee)
        employeeTable.write
        employeeTable.read

        employeeTableModel.clear
        employeeTableModel ++= employeeTable.list

        fioTextField.clear
        salaryTextField.clear
      }
    }

    val hbox = new HBox {
      content = List(fioTextField, salaryTextField, addButton)
    }

    scene = new Scene {
      content = new VBox {
        content = List(label, table, hbox)
        spacing = 10
        padding = Insets(10, 10, 10, 10)
      }
    }
  }
}
