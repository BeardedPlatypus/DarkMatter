package com.beardedplatypus.world.geometry

import com.beardedplatypus.math.{Point3d, Ray, Vector3d}
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.acceleration_structures.BoundingBoxable

abstract sealed class GeometricObject(val material: Material, val castsShadow: Boolean) {
  def intersect(ray: Ray): Option[RayResult]
  def intersectDistance(ray: Ray): Option[Double]
}

abstract case class FiniteGeometricObject(mat: Material, cShadow: Boolean) extends GeometricObject(mat, cShadow) with BoundingBoxable
abstract case class InfiniteGeometricObject(mat: Material, cShadow: Boolean) extends GeometricObject(mat, cShadow)