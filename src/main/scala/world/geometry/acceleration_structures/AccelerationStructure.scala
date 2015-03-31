package com.beardedplatypus.world.geometry.acceleration_structures

import com.beardedplatypus.math.Ray
import com.beardedplatypus.shading.RayResult

abstract class AccelerationStructure {
  def intersectDistance(ray: Ray): Option[Double]
  def intersect(ray: Ray): Option[RayResult]
  def isVisible(ray: Ray, distance: Double): Boolean
}
