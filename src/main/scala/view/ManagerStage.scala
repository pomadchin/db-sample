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

class ManagerStage extends VStage {
  val managerTable     = ManagerTable
  val managerTaskTable = ManagerTaskTable
  val taskTable        = TaskTable

  managerTable.read
  managerTaskTable.read
  taskTable.read

  val managerTableModel = new ObservableBuffer[Manager]
  managerTableModel ++= managerTable.list

  title = "Scala db Sample"
  val label = new Label("Manager Table") {
    font = Font("Arial", 20)
  }

  val table = new TableView[Manager](managerTableModel) {
    columns ++= List(
      new TableColumn[Manager, String] {
        text = "Id"
        cellValueFactory = { c => new StringProperty(this, "id", c.value.id.getOrElse(0).toString) }
        cellFactory = _ => new TextFieldTableCell[Manager, String] { alignment = Pos.CENTER }; (new DefaultStringConverter)
        prefWidth = 40
      },
      new TableColumn[Manager, String] {
        text = "Name"
        cellValueFactory = { _.value.vFio }
        cellFactory = _ => new TextFieldTableCell[Manager, String] (new DefaultStringConverter)
        onEditCommit = (evt: CellEditEvent[Manager, String]) => {
          val manager = evt.rowValue
          val newLastFioVal = evt.newValue
          // Update current person data set
          println(manager.toString + " " + newLastFioVal)
          println(manager.id.getOrElse(0).toString)
        }
        prefWidth = 180
      },
      new TableColumn[Manager, String] {
        text = "Position"
        cellValueFactory = { _.value.vPosition }
        cellFactory = _ => new TextFieldTableCell[Manager, String] (new DefaultStringConverter)
        onEditCommit = (evt: CellEditEvent[Manager, String]) => {
          val manager = evt.rowValue
          val newLastPositionVal = evt.newValue
          // Update current person data set
          println(manager.toString + " " + newLastPositionVal)
          println(manager.id.getOrElse(0).toString)
        }
        prefWidth = 180
      },
      new TableColumn[Manager, Boolean] {
        text = "Action"
        cellValueFactory = { e => ObjectProperty[Boolean](e.value != null) }
        cellFactory = _ => new TableCell[Manager, Boolean] {
          alignment = Pos.CENTER
          item.onChange((_, _, p) =>
            if(p) {
              graphic = new HBox {
                content = List(
                  new Button("Delete") {
                    onAction = (ae: ActionEvent) => {
                      val mi = managerTableModel.get(index.value).id.getOrElse(0)

                      if(index.value < managerTableModel.length) {
                        managerTable.Delete(mi)

                        // cascade remove
                        val managerTasks = managerTaskTable.list.filter(_.sourceId == mi)
                        val managerTaskIds = managerTasks.map(_.targetId)
                        val tasks = taskTable.list.filter(t => (List(t.id.getOrElse(0)) intersect managerTaskIds).length > 0)

                        tasks.foreach(t => taskTable.Delete(t.id.getOrElse(0)))
                        managerTasks.foreach(t => {
                          managerTaskTable.DeleteLink(mi, t.id.getOrElse(0))
                          taskTable.Delete(t.id.getOrElse(0))
                        })

                        refreshTableView
                      }
                    }
                  },
                  new Button("Add Tasks") {
                    onAction = (ae: ActionEvent) => {
                      val tasksStage = new ManagerTaskStage(managerTableModel.get(index.value).id)
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

  val positionTextField = new TextField {
    promptText = "Position"
    maxWidth = 180
  }

  val addButton = new Button("Add") {
    onAction = (_:ActionEvent) => {
      val manager = Manager(fioTextField.getText, positionTextField.getText)

      managerTable.Add(manager)
      refreshTableView
    }
  }

  val hbox = new HBox {
    content = List(fioTextField, positionTextField, addButton)
    spacing = 10
  }

  val vbox = new VBox {
    content = List(label, table, hbox)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
  }

  scene = new Scene {
    content = vbox
  }

  def refreshTableView = {
    managerTable.write
    taskTable.write
    managerTaskTable.write
    managerTable.read
    taskTable.read
    managerTaskTable.read

    managerTableModel.clear
    managerTableModel ++= managerTable.list

    fioTextField.clear
    positionTextField.clear
  }
}
