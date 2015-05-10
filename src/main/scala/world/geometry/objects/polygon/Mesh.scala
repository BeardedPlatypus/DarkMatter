package com.beardedplatypus.world.geometry.objects.polygon

import com.beardedplatypus.math.{Transformation, Ray}
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.acceleration_structures.{AABBTree, AccelerationStructure}
import com.beardedplatypus.world.geometry.objects.FiniteGeometricObject

// TODO make triangleStructure more general
class Mesh(triangleStructure: AABBTree, val transformation: Transformation, tMat: Material, tShadow: Boolean) extends FiniteGeometricObject(tMat, tShadow) {
  override def intersect(ray: Ray): Option[RayResult] = {
    val rayLocal = transformation transformInv ray
    val intersectionOption = boundingBox.intersect(rayLocal)

    if (intersectionOption.isEmpty) None
    else {
      val intersection = intersectionOption.get
      Option(RayResult(intersection.localHitPoint,
                       transformation transform intersection.worldHitPoint,
                       (transformation transformInvTranspose intersection.normal).normalized,
                       intersection.uv,
                       intersection.material,
                       ray,
                       intersection.distance))
    }
  }
  override def intersectDistance(ray: Ray): Option[Double] = boundingBox.intersectDistance(transformation transformInv ray)

  val boundingBox = triangleStructure.root
}