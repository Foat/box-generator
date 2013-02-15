package tools.generate

import java.awt.GraphicsDevice
import swing._
import org.interactivemesh.scala.swing.j3d.SCanvas3D
import swing.event.ButtonClicked
import swing.Swing._

/**
 * @author Foat Akhmadeev
 */
final class GeneratorPanel(gd: GraphicsDevice) extends BoxPanel(Orientation.Horizontal) {
  border = Swing.EmptyBorder(0, 50, 0, 0)

  import SceneCreator._

  private val universe = new GeneratorUniverse(gd, transformGroup)

  //controls
  private object navigatePanel extends BoxPanel(Orientation.Vertical) {
    val snapshot = new Button("Do snapshot")
    //sets random positions for all small boxes
    val random = new Button("Random pos")
    //change main box size
    val bxAdd = new Button("+")
    val bxSub = new Button("-")
    val byAdd = new Button("+")
    val bySub = new Button("-")
    val bzAdd = new Button("+")
    val bzSub = new Button("-")
    listenTo(snapshot, random, bxAdd, bxSub, byAdd, bySub, bzAdd, bzSub)

    import Box3D._

    reactions += {
      case ButtonClicked(`snapshot`) => {
        universe.doSnapshot(Constants.snapshotName)
      }
      case ButtonClicked(`random`) => {
        randomPos()
      }
      case ButtonClicked(`bxAdd`) => {
        mainBox + SCALE_X
      }
      case ButtonClicked(`bxSub`) => {
        mainBox - SCALE_X
      }
      case ButtonClicked(`byAdd`) => {
        mainBox + SCALE_Y
      }
      case ButtonClicked(`bySub`) => {
        mainBox - SCALE_Y
      }
      case ButtonClicked(`bzAdd`) => {
        mainBox + SCALE_Z
      }
      case ButtonClicked(`bzSub`) => {
        mainBox - SCALE_Z
      }
    }

    border = EmptyBorder(10)
    maximumSize = new Dimension(250, 200)
    contents +=(
      new BoxPanel(Orientation.Horizontal) {
        contents +=(bxAdd, new Label(" X "), bxSub)
      },
      VStrut(10),
      new BoxPanel(Orientation.Horizontal) {
        contents +=(byAdd, new Label(" Y "), bySub)
      },
      VStrut(10),
      new BoxPanel(Orientation.Horizontal) {
        contents +=(bzAdd, new Label(" Z "), bzSub)
      },
      VStrut(10),
      new BoxPanel(Orientation.Horizontal) {
        contents +=(snapshot, HStrut(10), random)
      })
  }

  contents +=(new SCanvas3D(universe.canvas3d), navigatePanel)
}
