import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.Includes._
import scalafx.geometry.Insets
import scalafx.scene.control.SplitPane
import scalafx.scene.layout.{StackPane, Region}


object HiddenSplitPaneSample extends JFXApp {

  stage = new JFXApp.PrimaryStage {
    title = "Hidden Split Pane Example"
    width = 600
    height = 400
    scene = new Scene {
      root = {

        // Region that will be used in the split pane
        val reg1 = new Region {
          styleClass = List("rounded")
        }
        val reg2 = new Region {
          styleClass = List("rounded")
        }
        val reg3 = new Region {
          styleClass = List("rounded")
        }

        new StackPane {
          padding = Insets(20)
          content = new SplitPane {
            padding = Insets(20)
            dividerPositions_=(0.20, 0.80)
            items ++= Seq(reg1, reg2, reg3)
            id = "hiddenSplitter"
          }
        }
      }
    }
  }
}