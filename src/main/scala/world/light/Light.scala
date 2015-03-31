package com.beardedplatypus.world.light

import com.beardedplatypus.math.{Point3d, Vector3d}
import com.beardedplatypus.shading.Color

trait Light {
  def L(hitPoint: Point3d): Color
  def direction(hitPoint: Point3d): Vector3d
  def distanceTo(hitPoint: Point3d): Double
  val castsShadow: Boolean
}

class AmbientLight(val color: Color, val strength: Double) extends Light {
  override def L(hitPoint: Point3d): Color = this.color * this.strength
  override def direction(hitPoint: Point3d): Vector3d = throw new NotImplementedError()
  override def distanceTo(hitPoint: Point3d): Double = throw new NotImplementedError()
  override val castsShadow: Boolean = false
}

class DirectionLight(val color: Color, val strength: Double, val dir: Vector3d, shadow: Boolean) extends Light {
  override def L(hitPoint: Point3d): Color = this.color * this.strength
  override def direction(hitPoint: Point3d): Vector3d = dir.inverted.normalized
  override def distanceTo(hitPoint: Point3d): Double = Double.PositiveInfinity
  override val castsShadow: Boolean = shadow
}

class PointLight(val color: Color,
                 val strength: Double,
                 val position: Point3d,
                 val distanceAttenuation: (Point3d, Point3d) => Double,
                 shadow: Boolean) extends Light {
  override def L(hitPoint: Point3d): Color = this.color * this.strength * distanceAttenuation(this.position, hitPoint)
  override def direction(hitPoint: Point3d): Vector3d = (position - hitPoint).normalized
  override def distanceTo(hitPoint: Point3d): Double =  {
    val p: Vector3d = hitPoint - position
    Math.sqrt(p dot p)
  }

  override val castsShadow: Boolean = shadow
}