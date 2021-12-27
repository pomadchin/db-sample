package view

import scalafx.scene.layout.*
import scalafx.stage.Stage

trait VStage extends Stage:
  val vbox: VBox
  def refreshTableView: Unit
  def isNumeric(str: String): Boolean   = str.matches("""\d+(\.\d*)?""")
  def isAllDigits(str: String): Boolean = str forall Character.isDigit
