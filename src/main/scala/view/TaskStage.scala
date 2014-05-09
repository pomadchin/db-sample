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

class TaskStage extends VStage {

  TaskTable.read
  EmployeeTaskTable.read
  ManagerTaskTable.read

  val taskTableModel = new ObservableBuffer[Task]
  taskTableModel ++= TaskTable.list

  title = "Scala db Sample"
  val label = new Label("Task Table") {
    font = Font("Arial", 20)
  }

  val table = new TableView[Task](taskTableModel) {
    columns ++= List(
      new TableColumn[Task, String] {
        text = "Id"
        cellValueFactory = { c => new StringProperty(this, "id", c.value.id.getOrElse(0).toString) }
        cellFactory = _ => new TextFieldTableCell[Task, String] { alignment = Pos.CENTER }; (new DefaultStringConverter)
        prefWidth = 40
      },
      new TableColumn[Task, String] {
        text = "Name"
        cellValueFactory = { _.value.vName }
        cellFactory = _ => new TextFieldTableCell[Task, String] (new DefaultStringConverter())
        onEditCommit = (evt: CellEditEvent[Task, String]) => {
          val task = evt.rowValue
          val newNameVal = evt.newValue
          // Update current person data set
          println(task.toString + " " + newNameVal)
          println(task.id.getOrElse(0).toString)
        }
        prefWidth = 320
      },
      new TableColumn[Task, Boolean] {
        text = "Action"
        cellValueFactory = { e => ObjectProperty[Boolean](e.value != null) }
        cellFactory = _ => new TableCell[Task, Boolean] {
          alignment = Pos.CENTER
          item.onChange((_, _, p) =>
            if(p) {
              graphic = new HBox {
                content = List(
                  new Button("Delete") {
                    onAction = (ae: ActionEvent) => {
                      val ti = taskTableModel.get(index.value).id.getOrElse(0)

                      if(index.value < taskTableModel.length) {
                        TaskTable.Delete(ti)

                        // cascade remove
                        val employeeTasks = EmployeeTaskTable.list.filter(_.targetId == ti)
                        val managerTasks = ManagerTaskTable.list.filter(_.targetId == ti)

                        managerTasks.foreach(t => ManagerTaskTable.DeleteLink(t.sourceId, t.targetId))
                        employeeTasks.foreach(t => EmployeeTaskTable.DeleteLink(t.sourceId, t.targetId))

                        refreshTableView
                      }
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
        prefWidth = 320
      }
    )
    //editable = true
  }

  val nameTextField = new TextField {
    promptText = "Name"
    maxWidth = 180
  }

  val addButton = new Button("Add") {
    onAction = (_:ActionEvent) => {
      val task = Task(nameTextField.getText)

      TaskTable.Add(task)
      refreshTableView
    }
  }

  val nameSearchTextField = new TextField {
    promptText = "Name"
    maxWidth = 180
  }

  val searchButton = new Button("Search") {
    onAction = (_:ActionEvent) => {
      val name     = if(nameSearchTextField.getText.length > 0) Some(nameSearchTextField.getText) else None
      val taskList = TaskTable.find(name)

      taskTableModel.clear
      taskTableModel ++= taskList
    }
  }

  val refreshButton = new Button("Refresh") {
    onAction = (_:ActionEvent) => refreshTableView
  }

  val hbox = new HBox {
    content = List(nameTextField, addButton)
    spacing = 10
  }

  val hSearchBox = new HBox {
    content = List(nameSearchTextField, searchButton, refreshButton)
    spacing = 10
  }

  val vbox = new VBox {
    content = List(label, table, hbox, hSearchBox)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
  }

  scene = new Scene {
    content = vbox
  }

  def refreshTableView = {
    TaskTable.write
    EmployeeTaskTable.write
    ManagerTaskTable.write
    TaskTable.read
    EmployeeTaskTable.read
    ManagerTaskTable.read

    taskTableModel.clear
    taskTableModel ++= TaskTable.list

    nameTextField.clear
    nameSearchTextField.clear
  }
}
