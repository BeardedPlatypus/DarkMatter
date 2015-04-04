package world.geometry.objects.primitives

import com.beardedplatypus.math._
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material
import world.geometry.objects.FiniteGeometricObject

class Cylinder(t: Transformation, mat: Material, shadow: Boolean) extends Primitive(t, mat, shadow) {
  def normalAt(p: Point3d, w0: Vector3d): Vector3d = {
    val normalLocal: Vector3d = {
      if (Math.abs(Math.sqrt(p.x * p.x + p.z * p.z) - 1.0) < Cons.kEpsilon) new Vector3d(p.x, 0.0, p.z)
      else Vector3d(0.0, 1.0, 0.0)
    }.normalized

    lazy val normal: Vector3d = transformation.transformInvTranspose(normalLocal).normalized
    if ((normal dot w0) >= 0.0) normal else normal.inverted
  }

  def intersect(ray: Ray): Option[RayResult] = {
    val rayLocalCoord: Ray = this.t transformInv ray
    val tOption: Option[Double] = this.intersectDistance(ray)

    if (tOption.isDefined) {
      val t: Double = tOption.get
      val pLocal: Point3d = rayLocalCoord.origin + (rayLocalCoord.direction * t)
      val pWorld: Point3d = this.t transform pLocal
      Option(RayResult(pLocal, pWorld, this.normalAt(pLocal, ray.direction.inverted), Point2d.invalid, this.material, ray, t))
    } else None
  }

  // FIXME: REFACTOR THIS
  def intersectDistance(ray: Ray): Option[Double] = {
    lazy val rayLocal: Ray = this.t.transformInv(ray)

    lazy val a: Double = (rayLocal.direction.x * rayLocal.direction.x) + (rayLocal.direction.z * rayLocal.direction.z)
    lazy val b: Double = (rayLocal.origin.x * rayLocal.direction.x * 2.0) + (rayLocal.origin.z * rayLocal.direction.z * 2.0)
    lazy val c: Double = (rayLocal.origin.x * rayLocal.origin.x) + (rayLocal.origin.z * rayLocal.origin.z) - 1.0

    lazy val denom: Double = 1.0 / (2.0 * a)

    lazy val D:  Double = (b * b) - (4 * a * c)

    if (D < 0.0) return None

    lazy val sqrtD: Double = Math.sqrt(D)
    lazy val t0 = (-b - sqrtD) * denom
    lazy val t1 = (-b + sqrtD) * denom

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

  override lazy val boundingBox = this.getBoundingBox
  protected override val bbRadius: Double = 1.0
}