package com.beardedplatypus.world.geometry.objects.primitives

import com.beardedplatypus.math._
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material

class Cube(t: Transformation, mat: Material, shadow: Boolean) extends Primitive(t, mat, shadow) {
  def intersect(ray: Ray): Option[RayResult] = {
    val rayLocal: Ray = this.t transformInv ray

    var t: (Double, Double) = ((-0.5 - rayLocal.origin.x) * ray.invDirection.x, (0.5 - rayLocal.origin.x) * ray.invDirection.x)
    var tOption: Option[Double] = None
    if (t._1 > t._2) t = t.swap
    var normalLocal: Vector3d = Vector3d(1.0, 0.0, 0.0)

    var ty: (Double, Double) = ((-0.5 - rayLocal.origin.y) * ray.invDirection.y, (0.5 - rayLocal.origin.y) * ray.invDirection.y)

    if (ty._1 > ty._2) ty = ty.swap
    if (t._1 > ty._2 || ty._1 > t._2) return None

    if (ty._1 > t._1) {
      normalLocal = Vector3d(0.0, 1.0, 0.0)
    }
    t = (Math.max(t._1, ty._1), Math.min(ty._2, t._2))

    val tz: (Double, Double) = ((-0.5 - rayLocal.origin.z) * ray.invDirection.z, (0.5 - rayLocal.origin.z) * ray.invDirection.z)
    if (tz._1 > tz._2) t = t.swap
    if (t._1 > tz._2 || tz._1 > t._2) return None

    if (tz._1 > t._1) {
      normalLocal = Vector3d(0.0, 0.0, 1.0)
    }
    t = (Math.max(t._1, tz._1), Math.min(tz._2, t._2))
    tOption = Option(t._1)

    lazy val normalT: Vector3d = transformation.transformInvTranspose(normalLocal.normalized).normalized
    lazy val normal = if ((normalT dot ray.direction.inverted) >= 0.0) normalT else normalT.inverted

    if (tOption.isDefined) {
      val t: Double = tOption.get
      val pLocal: Point3d = rayLocal.origin + (rayLocal.direction * t)
      val pWorld: Point3d = this.t transform pLocal
      Option(RayResult(pLocal, pWorld, normal, Point2d.invalid, this.material, ray, t))
    } else None
  }

  def intersectDistance(ray: Ray): Option[Double] = {
    val rayLocal: Ray = this.t.transformInv(ray)
    var t: (Double, Double) = ((-0.5 - rayLocal.origin.x) * ray.invDirection.x, (0.5 - rayLocal.origin.x) * ray.invDirection.x)
    if (t._1 > t._2) t = t.swap
    var ty: (Double, Double) = ((-0.5 - rayLocal.origin.y) * ray.invDirection.y, (0.5 - rayLocal.origin.y) * ray.invDirection.y)

    if (ty._1 > ty._2) ty = ty.swap
    if (t._1 > ty._2 || ty._1 > t._2) return None
    t = (Math.max(t._1, ty._1), Math.min(ty._2, t._2))

    val tz: (Double, Double) = ((-0.5 - rayLocal.origin.z) * ray.invDirection.z, (0.5 - rayLocal.origin.z) * ray.invDirection.z)
    if (tz._1 > tz._2) t = t.swap
    if (t._1 > tz._2 || tz._1 > t._2) return None

    t = (Math.max(t._1, tz._1), Math.min(tz._2, t._2))
    Option(t._1)
  }

  override lazy val boundingBox = this.getBoundingBox
  protected override val bbRadius: Double = 0.5
}
