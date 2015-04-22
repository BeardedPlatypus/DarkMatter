package com.beardedplatypus.world.geometry.objects.primitives

import com.beardedplatypus.math._
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.acceleration_structures.AABBNode
import com.beardedplatypus.world.geometry.objects.FiniteGeometricObject

class FinitePlane(val transformation: Transformation,
                  material: Material,
                  castShadow: Boolean) extends FiniteGeometricObject(mat = material,
                                                                     cShadow = castShadow) {
  protected val normalLocal = Vector3d(0.0, 1.0, 0.0)

  def intersect(ray: Ray): Option[RayResult] = {
    val tOption = intersectDistance(ray)

    if (tOption.isEmpty) None
    else {
      val t = tOption.get
      val rayLocalCoord: Ray = transformation.transformInv(ray)
      val pLocal: Point3d = rayLocalCoord.origin + (rayLocalCoord.direction * t)

      if (Math.abs(pLocal.x) > 0.5 || Math.abs(pLocal.z) > 0.5 ) None
      else {
        Option(RayResult(pLocal,
                         transformation.transform(pLocal),
                         transformation.transformInvTranspose(normalLocal).normalized,
                         Point2d.invalid,
                         this.material,
                         ray,
                         t))
      }
    }
  }

  def intersectDistance(ray: Ray): Option[Double] = {
    val rayLocalCoord: Ray = transformation.transformInv(ray)
    val t: Double = (normalLocal dot (rayLocalCoord.origin * -1.0)) / (normalLocal dot rayLocalCoord.direction)
    if (t > Cons.kEpsilon) Option(t) else None
  }

  val boundingBox: AABBNode = {
    val points = List(new Point3d(-0.5, 0.0, -0.5),
                      new Point3d(-0.5, 0.0, 0.5),
                      new Point3d(0.5, 0.0, -0.5),
                      new Point3d(0.5, 0.0, 0.5))
    val transformHead = transformation transform points.head

    var boundsX0 = transformHead.x
    var boundsX1 = transformHead.x

    var boundsY0 = transformHead.y
    var boundsY1 = transformHead.y

    var boundsZ0 = transformHead.z
    var boundsZ1 = transformHead.z

    for (p <- points.tail) {
      val transformedPoint = transformation transform p

      if (transformedPoint.x < boundsX0) boundsX0 = transformedPoint.x
      else if (transformedPoint.x > boundsX1) boundsX1 = transformedPoint.x

      if (transformedPoint.y < boundsY0) boundsY0 = transformedPoint.y
      else if (transformedPoint.y > boundsY1) boundsY1 = transformedPoint.y

      if (transformedPoint.z < boundsZ0) boundsZ0 = transformedPoint.z
      else if (transformedPoint.z > boundsZ1) boundsZ1 = transformedPoint.z
    }

    AABBNode.leaf(new Point3d(boundsX0, boundsY0, boundsZ0),
                  new Point3d(boundsX1, boundsY1, boundsZ1),
                  this)
  }
}