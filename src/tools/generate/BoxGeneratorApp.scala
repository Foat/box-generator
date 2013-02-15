package tools.generate

import swing.{Frame, SwingApplication}
import java.awt.{Point, Dimension, Toolkit}

/**
 * @author Foat Akhmadeev
 */
object BoxGeneratorApp extends SwingApplication {
  override def startup(args: Array[String]) {
    val appPanel = new GeneratorPanel(mainFrame.peer.getGraphicsConfiguration.getDevice)
    mainFrame.contents = appPanel
    mainFrame.pack()
    mainFrame.visible = true
  }

  private val screenSize = Toolkit.getDefaultToolkit.getScreenSize

  private object mainFrame extends Frame {
    title = "Box Generator"
    // Frame size and location
    private val frameHeight = (screenSize.height * 0.8).asInstanceOf[Int]
    private val frameWidth = Math.min((frameHeight * 1.4).asInstanceOf[Int], screenSize.width)
    preferredSize = new Dimension(frameWidth, frameHeight)
    location = new Point((screenSize.width - frameWidth) / 2, (screenSize.height - frameHeight) / 2)

    override def closeOperation() {
      closeAppOperation()
    }
  }

  private def closeAppOperation() {
    mainFrame.visible = false
    mainFrame.dispose()
    System.exit(0)
  }
}