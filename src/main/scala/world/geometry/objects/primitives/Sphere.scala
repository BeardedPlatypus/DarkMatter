package com.beardedplatypus.world.geometry.objects.primitives

import com.beardedplatypus.math._
import com.beardedplatypus.shading.RayResultLocal
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.acceleration_structures.AABBNode

// Unit sphere, radius is 1.0, scale with transformation.
class Sphere(t: Transformation, mat: Material, cs: Boolean) extends Primitive(t, mat, cs){

  override protected def intersectLocal(rayLocal: Ray): Option[RayResultLocal] = {
    val tOption: Option[Double] = this.intersectDistanceLocal(rayLocal)

    if (tOption.nonEmpty) {
      val t: Double = tOption.get
      val pLocal: Point3d = rayLocal.origin + (rayLocal.direction * t)
      Option(RayResultLocal(pLocal, this.normalAt(pLocal, rayLocal.direction.inverted), Point2d.invalid, t))
    } else None
  }

  override protected def intersectDistanceLocal(rayLocal: Ray): Option[Double] = {
    val dirLocalCoord: Vector3d = rayLocal.direction
    val oLocalCoord: Point3d = rayLocal.origin
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
    val normal: Vector3d = pLocal.toVector3d.normalized
    if ((normal dot w0) >= 0.0) normal else normal.inverted
  }

  protected override val bbRadius: Double = 1.0
  override lazy val boundingBox: AABBNode = this.getBoundingBox
}