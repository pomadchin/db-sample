package view

import scalafx.scene.layout._
import scalafx.stage.Stage

trait VStage extends Stage {
  val vbox: VBox
  def refreshTableView
}
