package com.beardedplatypus.world.light

import com.beardedplatypus.math.{Point3d, Vector3d}
import com.beardedplatypus.shading.{RayResult, Color}

trait Light {
  def L(rayResult: RayResult, direction: Vector3d): Color
  def G(pHit: Point3d, pSample: Point3d): Double = 1.0
  def pdfDiv(pSample: Point3d) = 1.0

  def sample(hitPoint: Point3d): List[LightSample]

  val castsShadowLight: Boolean
}

class AmbientLight(val color: Color, val strength: Double) extends Light {
  override def L(rayResult: RayResult, direction: Vector3d): Color = this.color * this.strength
  override def sample(hitPoint: Point3d): List[LightSample] = throw new NotImplementedError()

  override val castsShadowLight: Boolean = false
}

class DirectionLight(val color: Color, val strength: Double, val dir: Vector3d, shadow: Boolean) extends Light {
  override def L(rayResult: RayResult, direction: Vector3d): Color = this.color * this.strength
  override def sample(hitPoint: Point3d): List[LightSample] = samples

  override val castsShadowLight: Boolean = shadow

  private val direction = dir.inverted.normalized
  private val samples = List(new LightSample(Point3d.NaN, direction, Double.PositiveInfinity))
}

class PointLight(val color: Color,
                 val strength: Double,
                 val position: Point3d,
                 val distanceAttenuation: (Point3d, Point3d) => Double,
                 shadow: Boolean) extends Light {
  override def L(rayResult: RayResult, direction: Vector3d): Color = this.color * this.strength * distanceAttenuation(this.position, rayResult.worldHitPoint)
  override def sample(hitPoint: Point3d) = List(new LightSample(position, direction(hitPoint), distanceTo(hitPoint)))

  private def direction(hitPoint: Point3d): Vector3d = (position - hitPoint).normalized
  private def distanceTo(hitPoint: Point3d): Double =  {
    val p: Vector3d = hitPoint - position
    Math.sqrt(p dot p)
  }

  override val castsShadowLight: Boolean = shadow
}