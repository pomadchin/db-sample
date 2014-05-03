import core.db.{EmployeeTask, EmployeeTaskTable}
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.scene.Scene
import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.image.Image
import scalafx.scene.image.ImageView
import scalafx.scene.layout.AnchorPane
import scalafx.scene.layout.BorderPane
import scalafx.scene.layout.VBox
import scalafx.scene.paint.Color
import scalafx.scene.shape.Rectangle
import scalafx.scene.control.TableColumn._
import scalafx.scene.control.{TableColumn, TableView}
import scala.language.implicitConversions

/** An example of  a BorderPane layout, with placement of children in the top,
  * left, center, right, and bottom positions.
  *
  * @see scalafx.scene.layout.BorderPane
  * @resource /scalafx/ensemble/images/icon-48x48.png
  */
object BorderPaneSample extends JFXApp {

  implicit def lc[T](l: List[T]) = ObservableBuffer[T](l)

  val employeeTaskTable = EmployeeTaskTable
  employeeTaskTable.read

  stage = new JFXApp.PrimaryStage {
    title = "Border Pane Example"
    width = 400
    height = 400
    resizable = false
    scene = new Scene {
      root = {

        // Left content using VBox
        val leftVBox = new VBox {
          spacing = 10
          content = List(Label("Left Hand"), Label("Choice One"), Label("Choice Two"), Label("Choice Three"))
        }

        // Center content using Anchor Pane 
        val centerLabel = Label("We're in the center area.")

        val centerAnchorPane = new AnchorPane {
          content = List(
            centerLabel,
            new TableView[EmployeeTask](employeeTaskTable.list) {
              columns ++= List(
                new TableColumn[EmployeeTask, String] {
                  text = "sourceId"
                  cellValueFactory = { _.value.vSourceId }
                  prefWidth = 180
                },
                new TableColumn[EmployeeTask, String] {
                  text = "targetId"
                  cellValueFactory = { _.value.vTargetId }
                  prefWidth = 180
                }
              )
            }
          )
        }

        // Right content using VBox
        val rightVBox = new VBox {
          spacing = 10
          content = List(Label("Right Hand"), Label("Thing A"), Label("Thing B"), Label("Thing C"))
        }

        // Right content
        val bottomLabel = Label("I am a status message. I am at the bottom")

        new BorderPane {
          maxWidth = 400
          maxHeight = 400
          padding = Insets(20)
          center = centerAnchorPane
          right = rightVBox
          bottom = bottomLabel
          resizable = false
        }
      }
    }
  }
}