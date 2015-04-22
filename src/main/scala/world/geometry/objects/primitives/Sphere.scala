package com.beardedplatypus.world.geometry.objects.primitives

import com.beardedplatypus.math._
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.acceleration_structures.AABBNode

// Unit sphere, radius is 1.0, scale with transformation.
class Sphere(t: Transformation, mat: Material, cs: Boolean) extends Primitive(t, mat, cs){

  override def intersect(ray: Ray): Option[RayResult] = {
    val rayLocalCoord: Ray = this.transformation transformInv ray
    val tOption: Option[Double] = this.intersectDistance(ray)

    if (tOption.nonEmpty) {
      val t: Double = tOption.get
      val pLocal: Point3d = rayLocalCoord.origin + (rayLocalCoord.direction * t)
      val pWorld: Point3d = transformation.transform(pLocal)
      Option(RayResult(pLocal, pWorld, this.normalAt(pLocal, ray.direction.inverted), Point2d.invalid, this.material, ray, t))
    } else None
  }

  override def intersectDistance(ray: Ray): Option[Double] = {
    val rayLocalCoord: Ray = this.transformation transformInv ray
    val dirLocalCoord: Vector3d = rayLocalCoord.direction
    val oLocalCoord: Point3d = rayLocalCoord.origin
    val a: Double = dirLocalCoord dot dirLocalCoord
    val b: Double = (dirLocalCoord dot oLocalCoord) * 2.0
    val c: Double = (oLocalCoord dot oLocalCoord) - 1.0 //radius * radius

    val d: Double = b * b - 4.0 * a * c

    if (d < 0.0) None
    else {
      val e: Double = Math.sqrt(d)
      val invDenom: Double = 1.0 / (2.0 * a)

      val tSmall = (-b - e) * invDenom
      if (tSmall > Cons.kEpsilon) Option(tSmall)
      else {
        val tBig = (-b + e) * invDenom
        if (tBig > Cons.kEpsilon) Option(tBig)
        else None
      }
    }
  }

  def normalAt(pLocal: Point3d, w0: Vector3d): Vector3d = {
    val normal: Vector3d = transformation.transformInvTranspose(pLocal.toVector3d.normalized).normalized
    if ((normal dot w0) >= 0.0) normal else normal.inverted
  }

  protected override val bbRadius: Double = 1.0
  override lazy val boundingBox: AABBNode = this.getBoundingBox
}