package com.beardedplatypus.world.geometry.objects.primitives

import com.beardedplatypus.math._
import com.beardedplatypus.shading.RayResultLocal
import com.beardedplatypus.shading.material.Material

class Cylinder(t: Transformation, mat: Material, shadow: Boolean) extends Primitive(t, mat, shadow) {
  private def normalAt(p: Point3d, w0: Vector3d): Vector3d = {
    val normal: Vector3d = {
      if (Math.abs(Math.sqrt(p.x * p.x + p.z * p.z) - 1.0) < Cons.kEpsilon) new Vector3d(p.x, 0.0, p.z)
      else Vector3d(0.0, 1.0, 0.0)
    }.normalized

    if ((normal dot w0) >= 0.0) normal else normal.inverted
  }

  override protected def intersectLocal(rayLocal: Ray): Option[RayResultLocal] = {
    val tOption: Option[Double] = this.intersectDistanceLocal(rayLocal)

    if (tOption.isDefined) {
      val t: Double = tOption.get
      val pLocal: Point3d = rayLocal.origin + (rayLocal.direction * t)
      Option(RayResultLocal(pLocal, this.normalAt(pLocal, rayLocal.direction.inverted), Point2d.invalid, t))
    } else None
  }

  // FIXME: REFACTOR THIS
  override protected def intersectDistanceLocal(rayLocal: Ray): Option[Double] = {
    val a: Double = (rayLocal.direction.x * rayLocal.direction.x) + (rayLocal.direction.z * rayLocal.direction.z)
    val b: Double = (rayLocal.origin.x * rayLocal.direction.x * 2.0) + (rayLocal.origin.z * rayLocal.direction.z * 2.0)
    val c: Double = (rayLocal.origin.x * rayLocal.origin.x) + (rayLocal.origin.z * rayLocal.origin.z) - 1.0

    val denom: Double = 1.0 / (2.0 * a)

    val D:  Double = (b * b) - (4 * a * c)

    if (D < 0.0) return None

    val sqrtD: Double = Math.sqrt(D)
    val t0 = (-b - sqrtD) * denom
    val t1 = (-b + sqrtD) * denom

    val tSmall = Math.min(t0, t1)
    val tBig = Math.max(t0, t1)

    val ySmall = rayLocal.origin.y + (tSmall * rayLocal.direction.y)
    val yBig = rayLocal.origin.y + (tBig * rayLocal.direction.y)

    if (ySmall < -1.0) {
      if (yBig < -1.0) None
      else {
        val th: Double = tSmall + (tBig - tSmall) * (ySmall + 1) / (ySmall - yBig)

        if (th <= 0.0) None
        else Option(th)
      }
    } else if (ySmall >= -1 && ySmall <= 1) {
      if (tSmall <= 0.0) None
      else Option(t0)
    } else if (ySmall > 1.0) {
      if (yBig > 1.0) None
      else {
        val th: Double = tSmall + (tBig - tSmall) * (ySmall - 1) / (ySmall - yBig)
        if (th <= 0.0) None
        else Option(th)
      }
    } else {
      None
    }
  }

  override val boundingBox = this.getBoundingBox
  protected override val bbRadius: Double = 1.0
}