package world.geometry.objects.polygon

import com.beardedplatypus.math.Ray
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.FiniteGeometricObject
import com.beardedplatypus.world.geometry.acceleration_structures.AABBNode

class Mesh(triangleStructure: AABBNode,tMat: Material, tShadow: Boolean) extends FiniteGeometricObject(tMat, tShadow) {
  override def intersect(ray: Ray): Option[RayResult] = boundingBox.intersect(ray)
  override def intersectDistance(ray: Ray): Option[Double] = boundingBox.intersectDistance(ray)

  lazy val boundingBox = triangleStructure
}