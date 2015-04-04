package world.geometry.objects.primitives

import com.beardedplatypus.math.{Transformation, Point3d}
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.acceleration_structures.AABBNode
import world.geometry.objects.FiniteGeometricObject

import scala.annotation.tailrec

abstract class Primitive(val transformation: Transformation, tMat: Material, tShadow: Boolean) extends FiniteGeometricObject(tMat, tShadow) {
  // TODO Refactor this.
  protected val bbRadius: Double

  protected def getBoundingBox: AABBNode = {
    @tailrec
    def constructBounds(localPoints: List[Point3d], accX0: Double, accY0: Double, accZ0: Double,
                                                    accX1: Double, accY1: Double , accZ1: Double): (Point3d, Point3d) = {
      localPoints match {
        case Nil => (new Point3d(accX0, accY0, accZ0), new Point3d(accX1, accY1, accZ1))
        case p :: ps => {
          val transformedPoint = transformation transform p

          val xs = if (transformedPoint.x < accX0) (transformedPoint.x, accX1)
                   else if (transformedPoint.x > accX1) (accX0, transformedPoint.x)
                   else (accX0, accX1)

          val ys = if (transformedPoint.y < accY0) (transformedPoint.y, accY1)
                   else if (transformedPoint.y > accY1) (accY0, transformedPoint.y)
                   else (accY0, accY1)

          val zs = if (transformedPoint.z < accZ0) (transformedPoint.z, accZ1)
                        else if (transformedPoint.z > accZ1) (accZ0, transformedPoint.z)
                        else (accZ0, accZ1)

          constructBounds(ps, xs._1, ys._1, zs._1, xs._2, ys._2, zs._2)
        }
      }
    }

    // construct unit cube vertices.
    val vertices: List[Point3d] = List(new Point3d(bbRadius, bbRadius, bbRadius),
                                       new Point3d(bbRadius, bbRadius, -bbRadius),
                                       new Point3d(bbRadius, -bbRadius, bbRadius),
                                       new Point3d(bbRadius, -bbRadius, -bbRadius),
                                       new Point3d(-bbRadius, bbRadius, bbRadius),
                                       new Point3d(-bbRadius, bbRadius, -bbRadius),
                                       new Point3d(-bbRadius, -bbRadius, bbRadius),
                                       new Point3d(-bbRadius, -bbRadius, -bbRadius))

    val transformedHead = transformation transform vertices.head
    val bounds = constructBounds(vertices.tail,
                                 transformedHead.x, transformedHead.y, transformedHead.z,
                                 transformedHead.x, transformedHead.y, transformedHead.z)
    AABBNode.leaf(bounds._1, bounds._2, this)
  }
}
