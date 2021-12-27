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
import scalafx.stage.Stage

class ManagerStage extends VStage:

  ManagerTable.read
  EmployeeTable.read
  ManagerTaskTable.read
  EmployeeTaskTable.read
  TaskTable.read

  val managerTableModel = new ObservableBuffer[Manager]
  managerTableModel ++= ManagerTable.list

  var sumSalary = new StringProperty(this, "sumSalary", "")

  title = "Scala db Sample"
  val label = new Label("Manager Table") {
    font = Font("Arial", 20)
  }

  val table = new TableView[Manager](managerTableModel) {
    columns ++= List(
      new TableColumn[Manager, String] {
        text = "Id"
        cellValueFactory = { c => new StringProperty(this, "id", c.value.id.getOrElse(0).toString) }
        cellFactory = (_: TableColumn[Manager, String]) => new TextFieldTableCell[Manager, String] { alignment = Pos.Center };
        new DefaultStringConverter
        prefWidth = 40
      },
      new TableColumn[Manager, String] {
        text = "Name"
        cellValueFactory = _.value.vFio
        cellFactory = (_: TableColumn[Manager, String]) => new TextFieldTableCell[Manager, String](new DefaultStringConverter)
        onEditCommit = (evt: CellEditEvent[Manager, String]) => {
          val manager       = evt.rowValue
          val newLastFioVal = evt.newValue
          // Update current person data set
          println(manager.toString + " " + newLastFioVal)
          println(manager.id.getOrElse(0).toString)
        }
        prefWidth = 180
      },
      new TableColumn[Manager, String] {
        text = "Position"
        cellValueFactory = _.value.vPosition
        cellFactory = (_: TableColumn[Manager, String]) => new TextFieldTableCell[Manager, String](new DefaultStringConverter)
        onEditCommit = (evt: CellEditEvent[Manager, String]) => {
          val manager            = evt.rowValue
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
        cellFactory = (_: TableColumn[Manager, Boolean]) =>
          new TableCell[Manager, Boolean] {
            alignment = Pos.Center
            item.onChange((_, _, p) =>
              if (p) {
                graphic = new HBox {
                  children = List(
                    new Button("Delete") {
                      onAction = (ae: ActionEvent) => {
                        val mi = managerTableModel.get(index.value).id.getOrElse(0)

                        if (index.value < managerTableModel.length) {
                          ManagerTable.DeleteCascade(mi)
                          refreshTableView
                        }
                      }
                    },
                    new Button("Add Tasks") {
                      onAction = (ae: ActionEvent) => {
                        val tasksStage = new ManagerTaskStage(managerTableModel.get(index.value).id)
                        tasksStage.show()
                      }
                    },
                    new Button("Sum Salary") {
                      onAction = (ae: ActionEvent) =>
                        if (index.value < managerTableModel.length) {
                          val sumSalary: Double = ManagerTable.sumSalary(index.value, managerTableModel)

                          val label = new Label("Manager employees sum salary: " + sumSalary) {
                            font = Font("Arial", 15)
                          }

                          val stage = new Stage {
                            title = "Sum Employees Salary"
                            scene = new Scene {
                              content = new HBox {
                                content = label
                                spacing = 10
                                padding = Insets(10, 10, 10, 10)
                              }
                            }
                          }

                          stage.show()
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
        prefWidth = 280
      }
    )
    // editable = true
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
    onAction = (_: ActionEvent) => {
      val manager = Manager(fioTextField.getText, positionTextField.getText)

      ManagerTable.Add(manager)
      refreshTableView
    }
  }

  val fioSearchTextField = new TextField {
    promptText = "Name"
    maxWidth = 180
  }

  val positionSearchTextField = new TextField {
    promptText = "Position"
    maxWidth = 180
  }

  val searchButton = new Button("Search") {
    onAction = (_: ActionEvent) => {
      val position    = if (positionSearchTextField.getText.nonEmpty) Some(positionSearchTextField.getText) else None
      val name        = if (fioSearchTextField.getText.nonEmpty) Some(fioSearchTextField.getText) else None
      val managerList = ManagerTable.find(name -> position)

      managerTableModel.clear()
      managerTableModel ++= managerList
    }
  }

  val refreshButton = new Button("Refresh") {
    onAction = (_: ActionEvent) => refreshTableView
  }

  val hbox = new HBox {
    children = List(fioTextField, positionTextField, addButton)
    spacing = 10
  }

  val hSearchBox = new HBox {
    children = List(fioSearchTextField, positionSearchTextField, searchButton, refreshButton)
    spacing = 10
  }

  val vbox = new VBox {
    children = List(label, table, hbox, hSearchBox)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
  }

  scene = new Scene {
    content = vbox
  }

  def refreshTableView =
    ManagerTable.write
    TaskTable.write
    ManagerTaskTable.write
    ManagerTable.read
    TaskTable.read
    ManagerTaskTable.read

    managerTableModel.clear()
    managerTableModel ++= ManagerTable.list

    fioTextField.clear()
    positionTextField.clear()
    fioSearchTextField.clear()
    positionSearchTextField.clear()

object ManagerStage:
  def apply() = new ManagerStage()
