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

  val taskTable = TaskTable
  val employeeTaskTable = EmployeeTaskTable
  val managerTaskTable = ManagerTaskTable

  taskTable.read
  employeeTaskTable.read
  managerTaskTable.read

  val taskTableModel = new ObservableBuffer[Task]
  taskTableModel ++= taskTable.list

  title = "Scala db Sample"
  val label = new Label("Task Table") {
    font = Font("Arial", 20)
  }

  val emptyLabel = new Label(" ") {
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
        prefWidth = 270
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
                        taskTable.Delete(ti)

                        // cascade remove
                        val employeeTasks = employeeTaskTable.list.filter(_.targetId == ti)
                        val managerTasks = managerTaskTable.list.filter(_.targetId == ti)

                        managerTasks.foreach(t => managerTaskTable.DeleteLink(t.sourceId, t.targetId))
                        employeeTasks.foreach(t => employeeTaskTable.DeleteLink(t.sourceId, t.targetId))

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
        prefWidth = 270
      }
    )
    //editable = true
  }

  val vbox = new VBox {
    content = List(label, table, emptyLabel)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
  }

  scene = new Scene {
    content = vbox
  }

  def refreshTableView = {
    taskTable.write
    employeeTaskTable.write
    managerTaskTable.write
    taskTable.read
    employeeTaskTable.read
    managerTaskTable.read

    taskTableModel.clear
    taskTableModel ++= taskTable.list
  }
}
