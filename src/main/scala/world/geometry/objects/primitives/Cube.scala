package com.beardedplatypus.world.geometry.objects.primitives

import com.beardedplatypus.math._
import com.beardedplatypus.shading.{RayResultLocal, RayResult}
import com.beardedplatypus.shading.material.Material

class Cube(t: Transformation, mat: Material, shadow: Boolean) extends Primitive(t, mat, shadow) {
  override def intersectLocal(rayLocal: Ray): Option[RayResultLocal] = {
    var t: (Double, Double) = ((-0.5 - rayLocal.origin.x) * rayLocal.invDirection.x, (0.5 - rayLocal.origin.x) * rayLocal.invDirection.x)
    if (t._1 > t._2) t = t.swap
    var normalLocal: Vector3d = Vector3d(1.0, 0.0, 0.0)

    var ty: (Double, Double) = ((-0.5 - rayLocal.origin.y) * rayLocal.invDirection.y, (0.5 - rayLocal.origin.y) * rayLocal.invDirection.y)

    if (ty._1 > ty._2) ty = ty.swap
    if (t._1 > ty._2 || ty._1 > t._2) return None

    if (ty._1 > t._1) {
      normalLocal = Vector3d(0.0, 1.0, 0.0)
    }
    t = (Math.max(t._1, ty._1), Math.min(ty._2, t._2))

    val tz: (Double, Double) = ((-0.5 - rayLocal.origin.z) * rayLocal.invDirection.z, (0.5 - rayLocal.origin.z) * rayLocal.invDirection.z)
    if (tz._1 > tz._2) t = t.swap
    if (t._1 > tz._2 || tz._1 > t._2) return None

    if (tz._1 > t._1) {
      normalLocal = Vector3d(0.0, 0.0, 1.0)
    }
    t = (Math.max(t._1, tz._1), Math.min(tz._2, t._2))

    val pLocal: Point3d = rayLocal.origin + (rayLocal.direction * t._1)
    Option(RayResultLocal(pLocal,
                          if ((normalLocal dot rayLocal.invDirection) >= 0.0) normalLocal else normalLocal.inverted,
                          Point2d.invalid, t._1))
  }

  def intersectDistanceLocal(rayLocal: Ray): Option[Double] = {
    var t: (Double, Double) = ((-0.5 - rayLocal.origin.x) * rayLocal.invDirection.x, (0.5 - rayLocal.origin.x) * rayLocal.invDirection.x)
    if (t._1 > t._2) t = t.swap
    var ty: (Double, Double) = ((-0.5 - rayLocal.origin.y) * rayLocal.invDirection.y, (0.5 - rayLocal.origin.y) * rayLocal.invDirection.y)

    if (ty._1 > ty._2) ty = ty.swap
    if (t._1 > ty._2 || ty._1 > t._2) return None
    t = (Math.max(t._1, ty._1), Math.min(ty._2, t._2))

    val tz: (Double, Double) = ((-0.5 - rayLocal.origin.z) * rayLocal.invDirection.z, (0.5 - rayLocal.origin.z) * rayLocal.invDirection.z)
    if (tz._1 > tz._2) t = t.swap
    if (t._1 > tz._2 || tz._1 > t._2) return None

    t = (Math.max(t._1, tz._1), Math.min(tz._2, t._2))
    Option(t._1)
  }

  override val boundingBox = this.getBoundingBox
  protected override val bbRadius: Double = 0.5
}
