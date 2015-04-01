package com.beardedplatypus.world.light

import com.beardedplatypus.math.{Vector3d, Point3d}
import com.beardedplatypus.shading.Color
import com.beardedplatypus.shading.material.EmissiveMaterial

trait AreaLight extends Light {
  val emissiveMaterial: EmissiveMaterial

  def L(hitPoint: Point3d): Color = throw new NotImplementedError()
  def direction(hitPoint: Point3d): Vector3d = throw new NotImplementedError()
  def distanceTo(hitPoint: Point3d): Double = throw new NotImplementedError()
}
