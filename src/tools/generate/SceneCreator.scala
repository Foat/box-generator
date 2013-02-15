package tools.generate

import javax.media.j3d.{PolygonAttributes, Transform3D, TransformGroup, Switch}
import javax.vecmath.Vector3f
import util.Random
import Constants.angle

/**
 * @author Foat Akhmadeev
 */
object SceneCreator {
  val mainBox = Box3D(Box3D.defaultAppearance(PolygonAttributes.CULL_FRONT))
  var boxArray = createBoxList(2)

  /**
   * create boxes with random scale
   * @param cnt number of boxes
   */
  def createBoxList(cnt: Int): Array[Box3D] = {
    val result = new Array[Box3D](cnt)
    for (i <- 0 until cnt)
      result(i) = generateBox
    result
  }

  //scene Switch with boxes
  private val mainSwitch = new Switch {
    setCapability(Switch.ALLOW_SWITCH_READ)
    setCapability(Switch.ALLOW_SWITCH_WRITE)
    setWhichChild(Switch.CHILD_ALL)

    addChild(mainBox)
    boxArray.foreach(addChild(_))
  }

  //scene TransformGroup
  val transformGroup = new TransformGroup {
    //for transforming
    setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE)
    setTransform(new Transform3D {
      //rotate group
      rotY(angle)
      //move group
      setTranslation(new Vector3f(.0f, .0f, -1.0f))
    })
    addChild(mainSwitch)
  }

  /**
   * generate box in mainBox
   */
  def generateBox: Box3D = {
    val maxScale = 0.25f
    val i = Random.nextInt(10) + 1
    val sizeX = (Random.nextFloat * i + mainBox.sizeX) / i * maxScale
    val sizeY = (Random.nextFloat * i + mainBox.sizeY) / i * maxScale
    val sizeZ = (Random.nextFloat * i + mainBox.sizeZ) / i * maxScale
    Box3D(sizeX, sizeY, sizeZ)
  }

  /**
   * set random position for current box
   */
  def randomPos(box: Box3D) {
    def getPos(rSize: Float, bSize: Float) = (Random.nextFloat - .5f) * (rSize - bSize)

    val posX = getPos(mainBox.sizeX, box.sizeX)
    val posY = getPos(mainBox.sizeY, box.sizeY)
    val posZ = getPos(mainBox.sizeZ, box.sizeZ)
    box.setStartPoint(posX, posY, posZ)
  }

  /**
   * set random position for box with specified index
   * @param index box index in [[tools.generate.SceneCreator.boxArray]]
   */
  def randomPos(index: Int) {
    try
      randomPos(boxArray(index))
    catch {
      case ex: IndexOutOfBoundsException => println("boxList size = " + boxArray.size + ", index = " + index)
    }
  }

  /**
   * set random position for all boxes in [[tools.generate.SceneCreator.boxArray]]
   */
  def randomPos() {
    boxArray.foreach(randomPos(_))
  }
}
