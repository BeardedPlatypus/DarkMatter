package com.beardedplatypus.world.geometry.objects.primitives

import com.beardedplatypus.math._
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.objects.InfiniteGeometricObject

class Plane(transformation: Transformation, normalLocal: Vector3d, mat: Material, cs: Boolean) extends InfiniteGeometricObject(mat, cs) {
  override def intersect(ray: Ray): Option[RayResult] = {
    val rayLocalCoord: Ray = transformation.transformInv(ray)
    val tOption: Option[Double] = this.intersectDistance(ray)

    if (tOption isDefined) {
      val t: Double = tOption.get
      val pLocal: Point3d = rayLocalCoord.origin + (rayLocalCoord.direction * t)
      val pWorld: Point3d = transformation.transform(pLocal)
      Option(RayResult(pLocal, pWorld, this.normalAt(pLocal, rayLocalCoord.direction.inverted), Point2d.invalid, this.material, ray, t))
    }
    else None
  }

  override def intersectDistance(ray: Ray): Option[Double] = {
    val rayLocalCoord: Ray = transformation.transformInv(ray)
    val t: Double = (normalLocal dot (rayLocalCoord.origin * -1.0)) / (normalLocal dot rayLocalCoord.direction)

    if (t > Cons.kEpsilon) Option(t) else None
  }

  def normalAt(p: Point3d, w0: Vector3d): Vector3d = {
    if ((normalLocal dot w0) >= 0.0) transformation.transformInvTranspose(normalLocal).normalized
    else transformation.transformInvTranspose(normalLocal).normalized.inverted
  }
}