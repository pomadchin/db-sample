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

class ManagerTaskStage(managerId: Option[Int] = None) extends VStage {

  ManagerTable.read
  ManagerTaskTable.read
  TaskTable.read

  val taskIds = ManagerTaskTable.GetTargetIds(managerId.getOrElse(0))

  val taskTableModel = new ObservableBuffer[Task]
  taskTableModel ++= TaskTable.list.filter(t => (List(t.id.getOrElse(0)) intersect taskIds).length > 0)

  title = "Scala db Sample"
  val label = new Label("Manager Task Table") {
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
          val employee = evt.rowValue
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
        cellFactory = _ => new TableCell[Task, Boolean] {
          alignment = Pos.CENTER
          item.onChange((_, _, p) =>
            if(p) {
              graphic = new HBox {
                content = List(
                  new Button("Delete") {
                    onAction = (ae: ActionEvent) => {
                      if(index.value < taskTableModel.length) {
                        ManagerTaskTable.DeleteLink(managerId.getOrElse(0), taskTableModel.get(index.value).id.getOrElse(0))
                        TaskTable.Delete(taskTableModel.get(index.value).id.getOrElse(0))
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
        prefWidth = 180
      }
    )
    //editable = true
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
    onAction = (_:ActionEvent) => {
      val task = Task(nameTextField.getText)
      ManagerTaskTable.AddLink(managerId.getOrElse(0), TaskTable.Add(task))

      refreshTableView
    }
  }

  val addIdButton = new Button("Add") {
    onAction = (_:ActionEvent) => {
      val taskId = if(isAllDigits(idTextField.getText)) idTextField.getText.toInt else 0

      val allTaskIds = TaskTable.list.map(_.id.getOrElse(0))

      if((allTaskIds intersect List(taskId)).length > 0)
        ManagerTaskTable.AddLink(managerId.getOrElse(0), taskId)

      refreshTableView
    }
  }

  val hbox = new HBox {
    content = List(nameTextField, addButton)
    spacing = 10
  }

  val hIdBox = new HBox {
    content = List(idTextField, addIdButton)
    spacing = 10
  }

  val vbox = new VBox {
    content = List(label, table, hbox, hIdBox)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
  }

  scene = new Scene {
    content = vbox
  }

  def refreshTableView = {
    TaskTable.write
    ManagerTaskTable.write
    TaskTable.read
    ManagerTaskTable.read

    val taskIds = ManagerTaskTable.list.filter(_.sourceId == managerId.getOrElse(0)).map(_.targetId)

    taskTableModel.clear
    taskTableModel ++= TaskTable.list.filter(t => (List(t.id.getOrElse(0)) intersect taskIds).length > 0)

    nameTextField.clear
    idTextField.clear
  }
}
