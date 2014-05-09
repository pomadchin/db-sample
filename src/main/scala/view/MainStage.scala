package view

import scalafx.application.JFXApp.PrimaryStage
import scalafx.collections.ObservableBuffer
import scalafx.scene.input._
import scalafx.geometry._
import scalafx.scene.control.cell.TextFieldTableCell
import scalafx.scene.Scene
import scalafx.scene.control.TableColumn._
import scalafx.scene.layout._
import scalafx.scene.control._
import scalafx.scene.text.Font
import scalafx.util.converter._
import scalafx.util._
import scalafx.beans.property._
import scalafx.Includes._
import scalafx.animation._

class MainStage extends PrimaryStage { outer =>
  val stages = Map(
    "EmployeeTable"          -> new EmployeeStage(),
    "ManagerTable"           -> new ManagerStage(),
    "TaskTable"              -> new TaskStage(),
    "EmployeeTaskILinkTable" -> new IEmployeeTaskStage(),
    "ManagerTaskILinkTable"  -> new IManagerTaskStage()
  )

  val emptyModel = new ObservableBuffer[String]
  emptyModel ++= List[String]()

  title = "Scala db Sample"
  val emptyLabel = new Label(" ") {
    font = Font("Arial", 20)
  }

  val emptyLabel2 = new Label(" ") {
    font = Font("Arial", 20)
  }

  val signLabel = new Label ("Apache License Version 2.0") {
    font = Font("Arial", 10)
  }

  val labelList = new Label("Table List") {
    font = Font("Arial", 20)
  }

  // empty table
  val emptyTable = new TableView[String](emptyModel) {
    columns ++= List(
      new TableColumn[String, String] {
        text = " "
        cellValueFactory = { _ => new StringProperty(this, "", "") }
        cellFactory = _ => new TextFieldTableCell[String, String] (new DefaultStringConverter)
        prefWidth = 580
      }
    )
    //editable = true
  }

  // empty layer
  var emptyVBox = new VBox {
    content = List(emptyLabel, emptyTable, emptyLabel2)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
  }

  val tableList: ListView[String] = new ListView[String] {
    prefWidth = 180

    items = ObservableBuffer(stages.keys.toList: _*)
    selectionModel().selectionMode = SelectionMode.SINGLE
    onMouseClicked = (evt: MouseEvent) => {
      val fadeInTransition = new FadeTransition {
        duration = Duration(500)
        node = emptyVBox
        fromValue = 1
        toValue = 0
      }
      fadeInTransition.play()

      val stage = stages(selectionModel().selectedItem.value)
      stage.refreshTableView

      val mHBox = new HBox {
        content = List(mainVBox, stage.vbox)
        spacing = 10
        padding = Insets(10, 10, 10, 10)
      }

      outer.scene = new Scene {
        content = new VBox{
          content = mHBox
        }
      }

      val fadeOutTransition = new FadeTransition {
        duration = Duration(500)
        node = stage.vbox
        fromValue = 0
        toValue = 1
      }
      fadeOutTransition.play()
    }

  }

  val mainVBox = new VBox {
    content = List(labelList, tableList)
    spacing = 10
    margin = Insets(10, 10, 10, 10)
  }

  val mHBox = new HBox {
    content = List(mainVBox, emptyVBox)
    spacing = 10
    padding = Insets(10, 10, 10, 10)
  }

  scene = new Scene {
    content = mHBox
  }
}
