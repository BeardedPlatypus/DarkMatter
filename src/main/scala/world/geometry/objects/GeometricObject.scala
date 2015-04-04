package com.beardedplatypus.world.geometry.objects

import com.beardedplatypus.math.Ray
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.acceleration_structures.BoundingBoxable

/**
 * Created by Month on 03/04/2015.
 */
abstract sealed class GeometricObject(val material: Material, val castsShadowObj: Boolean) {
  def intersect(ray: Ray): Option[RayResult]
  def intersectDistance(ray: Ray): Option[Double]
}

abstract case class InfiniteGeometricObject(mat: Material, cShadow: Boolean) extends GeometricObject(mat, cShadow)
abstract case class FiniteGeometricObject(mat: Material, cShadow: Boolean) extends GeometricObject(mat, cShadow) with BoundingBoxable
