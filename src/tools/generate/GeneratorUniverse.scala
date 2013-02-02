package tools.generate

import javax.media.j3d._
import com.sun.j3d.utils.universe.SimpleUniverse
import java.awt.{Color, GraphicsDevice}
import com.sun.j3d.utils.behaviors.mouse.{MouseWheelZoom, MouseTranslate, MouseRotate}
import javax.vecmath.{Point3d, Color3f}
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * @author Foat Akhmadeev
 */
final class GeneratorUniverse(gd: GraphicsDevice, scene: TransformGroup) {
  //scene background
  private val backgroundColor = Color.WHITE
  private val backgroundColor3f = new Color3f(backgroundColor)

  //main canvas
  val canvas3d = new Canvas3D(gd.getBestConfiguration(new GraphicsConfigTemplate3D)) {
    setBackground(backgroundColor)
  }

  //canvas for snapshot
  val offScreenCanvas3d = new Canvas3D(gd.getBestConfiguration(new GraphicsConfigTemplate3D), true) {
    def doSnapshot(fileName: String) {
      val loc = canvas3d.getLocationOnScreen
      setOffScreenLocation(loc)
      val dim = canvas3d.getSize
      doRender(dim.width, dim.height, fileName)
    }

    private def doRender(width: Int, height: Int, fileName: String) {
      val bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
      val buffer = new ImageComponent2D(ImageComponent.FORMAT_RGB, bImage)
      setOffScreenBuffer(buffer)
      renderOffScreenBuffer()
      waitForOffScreenRendering()
      val format = getFileExtension(fileName)
      //image file
      ImageIO.write(getOffScreenBuffer.getImage, format, new File(fileName))
    }
  }

  /**
   * @return file extension
   *         example:
   *         fileName = test.png
   *         result = png
   */
  private def getFileExtension(fileName: String) = fileName.drop(1 + fileName.lastIndexOf("."))

  /**
   * Create snapshot
   * @param fileName image name
   */
  def doSnapshot(fileName: String) {
    offScreenCanvas3d.doSnapshot(fileName)
  }

  private val offScreenScale = 1.0f
  private val sOn = canvas3d.getScreen3D
  private val sOff = offScreenCanvas3d.getScreen3D
  private val dim = sOn.getSize
  sOff.setSize(dim)
  sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth * offScreenScale)
  sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight * offScreenScale)

  //scene branch

  private val sceneBranch = new BranchGroup {
    setCapability(Group.ALLOW_CHILDREN_EXTEND)
    setCapability(Group.ALLOW_CHILDREN_WRITE)
    setCapability(BranchGroup.ALLOW_DETACH)

    val sphere = new BoundingSphere(new Point3d, java.lang.Double.MAX_VALUE)
    //mouse events
    val mouseRotate = new MouseRotate
    mouseRotate.setSchedulingBounds(sphere)
    mouseRotate.setTransformGroup(scene)

    val mouseTranslate = new MouseTranslate
    mouseTranslate.setSchedulingBounds(sphere)
    mouseTranslate.setTransformGroup(scene)

    val mouseWheelZoom = new MouseWheelZoom
    mouseWheelZoom.setSchedulingBounds(sphere)
    mouseWheelZoom.setTransformGroup(scene)
    //background
    val background = new Background {
      setCapability(Background.ALLOW_IMAGE_WRITE)
      setApplicationBounds(sphere)
      setColor(backgroundColor3f)
    }

    addChild(background)
    addChild(scene)
    addChild(mouseRotate)
    addChild(mouseTranslate)
    addChild(mouseWheelZoom)
  }

  private val universe = new SimpleUniverse(canvas3d) {
    getViewingPlatform.setNominalViewingTransform()
    addBranchGraph(sceneBranch)
  }

  //attach the offScreen canvas to the view
  universe.getViewer.getView.addCanvas3D(offScreenCanvas3d)
}