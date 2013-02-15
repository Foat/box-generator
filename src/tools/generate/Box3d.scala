package tools.generate

import javax.media.j3d._
import javax.vecmath.Color3f
import javax.media.j3d.GeometryArray._
import PolygonAttributes._
import Box3D._

/**
 * @author Foat Akhmadeev
 * @constructor create a new box with an appearance, starting point and size
 */
class Box3D(appearance: Appearance, var startX: Float, var startY: Float, var startZ: Float, var sizeX: Float, var sizeY: Float, var sizeZ: Float) extends Shape3D {
  /**
   * @constructor create a new box with an appearance and size (starting point = origin)
   */
  def this(appearance: Appearance, sizeX: Float, sizeY: Float, sizeZ: Float) {
    this(appearance, .0f, .0f, .0f, sizeX, sizeY, sizeZ)
  }

  setCapability(Shape3D.ALLOW_GEOMETRY_WRITE)
  setCapability(Shape3D.ALLOW_GEOMETRY_READ)

  setGeometry(createGeometry(startX, startY, startZ, sizeX, sizeY, sizeZ))
  setAppearance(appearance)
  setPickable(true)

  /**
   * sets the box size
   */
  def setSize(x: Float, y: Float, z: Float) {
    sizeX = x
    sizeY = y
    sizeZ = z
    setGeometry(createGeometry(startX, startY, startZ, x, y, z))
  }

  /**
   * sets the box starting point
   */
  def setStartPoint(sX: Float, sY: Float, sZ: Float) {
    startX = sX
    startY = sY
    startZ = sZ
    setGeometry(createGeometry(sX, sY, sZ, sizeX, sizeY, sizeZ))
  }

  /**
   * reduces the size of the box
   * @param direction see [[tools.generate.Box3D.SCALE_X]], [[tools.generate.Box3D.SCALE_Y]], [[tools.generate.Box3D.SCALE_Z]]
   */
  def -(direction: Int) {
    if ((direction & SCALE_X) != 0) sizeX -= 1
    if ((direction & SCALE_Y) != 0) sizeY -= 1
    if ((direction & SCALE_Z) != 0) sizeZ -= 1
    setSize(sizeX, sizeY, sizeZ)
  }

  /**
   * increases the size of the box
   * @param direction see [[tools.generate.Box3D.SCALE_X]], [[tools.generate.Box3D.SCALE_Y]], [[tools.generate.Box3D.SCALE_Z]]
   */
  def +(direction: Int) {
    if ((direction & SCALE_X) != 0) sizeX += 1
    if ((direction & SCALE_Y) != 0) sizeY += 1
    if ((direction & SCALE_Z) != 0) sizeZ += 1
    setSize(sizeX, sizeY, sizeZ)
  }
}

object Box3D {
  /**
   * creates a new box with a specified size
   */
  def apply(sizeX: Float, sizeY: Float, sizeZ: Float) = new Box3D(defaultAppearance(CULL_BACK), sizeX, sizeY, sizeZ)

  /**
   * creates a new box with a specified appearance
   */
  def apply(appearance: Appearance) = new Box3D(appearance, 1.0f, 1.0f, 1.0f)

  val SCALE_X = 0x01
  val SCALE_Y = 0x02
  val SCALE_Z = 0x04

  private val boxColor = new Color3f(Constants.shapeColor)

  //default coordinates
  private val coordinates = Array(
    -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f,     // back    +Z
    0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, // front   -Z
    0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,     // right   +X
    -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, // left    -X
    -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f,     // top     +Y
    -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f  // bottom  -Y
  )

  /**
   * creates a box with a specified starting point and size
   * @return geometry, see [[javax.media.j3d.QuadArray]]
   */
  def createGeometry(startX: Float, startY: Float, startZ: Float, sizeX: Float, sizeY: Float, sizeZ: Float): QuadArray = {
    createGeometry(COORDINATES, startX, startY, startZ, sizeX, sizeY, sizeZ)
  }

  /**
   * creates a box with a specified starting point, size and vertexFormat
   * @return geometry, see [[javax.media.j3d.QuadArray]]
   */
  def createGeometry(vertexFormat: Int, startX: Float, startY: Float, startZ: Float, sizeX: Float, sizeY: Float, sizeZ: Float): QuadArray = {
    val geometry = new QuadArray(24, vertexFormat)
    if (sizeX != 1.0f || sizeY != 1.0f || sizeZ != 1.0f) {
      val length = 24 * 3
      val sizeCoordinates = new Array[Float](length)
      var i = 0
      while (i < length) {
        sizeCoordinates(i) = sizeX * coordinates(i) + startX
        i += 1
        sizeCoordinates(i) = sizeY * coordinates(i) + startY
        i += 1
        sizeCoordinates(i) = sizeZ * coordinates(i) + startZ
        i += 1
      }
      geometry.setCoordinates(0, sizeCoordinates)
    }
    else
      geometry.setCoordinates(0, coordinates)

    geometry
  }

  /**
   * creates the default appearance with a specified face culling
   * @param cullFace see
   *                 [[javax.media.j3d.PolygonAttributes.CULL_BACK]],
   *                 [[javax.media.j3d.PolygonAttributes.CULL_FRONT]],
   *                 [[javax.media.j3d.PolygonAttributes.CULL_NONE]]
   * @return [[javax.media.j3d.Appearance]]
   */
  def defaultAppearance(cullFace: Int) = {
    //coloring attributes (shape color)
    val ca = new ColoringAttributes {
      setColor(boxColor)
    }
    //polygon type
    val pa = new PolygonAttributes {
      setPolygonMode(POLYGON_LINE)
      setCullFace(cullFace)
    }
    //rendering attributes
    val ra = new RenderingAttributes {
      setIgnoreVertexColors(true)
    }

    new Appearance {
      setRenderingAttributes(ra)
      setPolygonAttributes(pa)
      setColoringAttributes(ca)
    }
  }
}
