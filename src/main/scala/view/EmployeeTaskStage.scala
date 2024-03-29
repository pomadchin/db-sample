package view

import core.db.*
import core.models.*
import scalafx.collections.ObservableBuffer
import scalafx.event.ActionEvent
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.control.cell.TextFieldTableCell
import scalafx.scene.Scene
import scalafx.scene.control.TableColumn.*
import scalafx.scene.layout.*
import scalafx.scene.control.*
import scalafx.scene.text.Font
import scalafx.util.converter.*
import scalafx.beans.property.*
import scalafx.Includes.*

class EmployeeTaskStage(employeeId: Option[Int] = None) extends VStage:

  EmployeeTable.read
  EmployeeTaskTable.read
  TaskTable.read

  val taskIds = EmployeeTaskTable.GetTargetIds(employeeId.getOrElse(0))

  val taskTableModel = new ObservableBuffer[Task]
  taskTableModel ++= TaskTable.list.filter(t => (List(t.id.getOrElse(0)) intersect taskIds).nonEmpty)

  title = "Scala db Sample"
  val label = new Label("Employee Task Table") {
    font = Font("Arial", 20)
  }

  val table = new TableView[Task](taskTableModel) {
    columns ++= List(
      new TableColumn[Task, String] {
        text = "Id"
        cellValueFactory = { c => new StringProperty(this, "id", c.value.id.getOrElse(0).toString) }
        cellFactory = (_: TableColumn[Task, String]) => new TextFieldTableCell[Task, String] { alignment = Pos.Center }; new DefaultStringConverter
        prefWidth = 40
      },
      new TableColumn[Task, String] {
        text = "Name"
        cellValueFactory = _.value.vName
        cellFactory = (_: TableColumn[Task, String]) => new TextFieldTableCell[Task, String](new DefaultStringConverter())
        onEditCommit = (evt: CellEditEvent[Task, String]) => {
          val employee      = evt.rowValue
          val newLastFioVal = evt.newValue
          // Update current person data set
          println(employee.toString + " " + newLastFioVal)
          println(employee.id.getOrElse(0).toString)
        }
        prefWidth = 180
      },
      new TableColumn[Task, Boolean] {
        text = "Action"
        cellValueFactory = { e => ObjectProperty[Boolean](e.value != null) }
        cellFactory = (_: TableColumn[Task, Boolean]) =>
          new TableCell[Task, Boolean] {
            alignment = Pos.Center
            item.onChange((_, _, p) =>
              if (p) {
                graphic = new HBox {
                  children = List(
                    new Button("Delete") {
                      onAction = (ae: ActionEvent) =>
                        if (index.value < taskTableModel.length) {
                          EmployeeTaskTable.DeleteLink(employeeId.getOrElse(0), taskTableModel.get(index.value).id.getOrElse(0))
                          TaskTable.Delete(taskTableModel.get(index.value).id.getOrElse(0))
                          refreshTableView
                        }
                    }
                  )
                  spacing = 10
                  alignment = Pos.Center
                  // padding = Insets(10, 10, 10, 10)
                }
              }
            )
          }
        prefWidth = 180
      }
    )
    // editable = true
  }

  val nameTextField = new TextField {
    promptText = "Name"
    maxWidth = 180
  }

  val idTextField = new TextField {
    promptText = "Task Id"
    maxWidth = 180
  }

  val addButton = new Button("Add") {
    onAction = (_: ActionEvent) => {
      val task = Task(nameTextField.getText)
      EmployeeTaskTable.AddLink(employeeId.getOrElse(0), TaskTable.Add(task))

      refreshTableView
    }
  }

  val addIdButton = new Button("Add") {
    onAction = (_: ActionEvent) => {
      val taskId = if (isAllDigits(idTextField.getText)) idTextField.getText.toInt else 0

      val allTaskIds = TaskTable.list.map(_.id.getOrElse(0))

      if ((allTaskIds intersect List(taskId)).nonEmpty)
        EmployeeTaskTable.AddLink(employeeId.getOrElse(0), taskId)

      refreshTableView
    }
  }

  val hbox = new HBox {
    children = List(nameTextField, addButton)
    spacing = 10
  }

  val hIdBox = new HBox {
    children = List(idTextField, addIdButton)
    spacing = 10
  }

  val vbox = new VBox {
    children = List(label, table, hbox, hIdBox)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
  }

  scene = new Scene {
    content = vbox
  }

  def refreshTableView =
    TaskTable.write
    EmployeeTaskTable.write
    TaskTable.read
    EmployeeTaskTable.read

    val taskIds = EmployeeTaskTable.list.filter(_.sourceId == employeeId.getOrElse(0)).map(_.targetId)

    taskTableModel.clear()
    taskTableModel ++= TaskTable.list.filter(t => (List(t.id.getOrElse(0)) intersect taskIds).nonEmpty)

    nameTextField.clear()
    idTextField.clear()

object EmployeeTaskStage:
  def apply() = new EmployeeTaskStage()
