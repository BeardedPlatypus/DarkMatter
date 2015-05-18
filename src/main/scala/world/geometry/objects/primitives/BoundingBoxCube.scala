//package com.beardedplatypus.world.geometry.objects.primitives
//
//import com.beardedplatypus.math._
//import com.beardedplatypus.shading.RayResult
//import com.beardedplatypus.shading.material.Material
//import com.beardedplatypus.world.geometry.acceleration_structures.AABBNode
//
//class BoundingBoxCube(val bounds: (Point3d, Point3d), t: Transformation, mat: Material, shadow: Boolean) extends Primitive(t, mat, shadow) {
//  def intersect(ray: Ray): Option[RayResult] = {
//    val rayLocal: Ray = this.t.transformInv(ray)
//
//    var tmin: Double = ((if (rayLocal.sign._1) bounds._2.x else bounds._1.x) - rayLocal.origin.x) * rayLocal.invDirection.x
//    var tmax: Double = ((if (rayLocal.sign._1) bounds._1.x else bounds._2.x) - rayLocal.origin.x) * rayLocal.invDirection.x
//
//    val tymin: Double = ((if (rayLocal.sign._2) bounds._2.y else bounds._1.y) - rayLocal.origin.y) * rayLocal.invDirection.y
//    val tymax: Double = ((if (rayLocal.sign._2) bounds._1.y else bounds._2.y) - rayLocal.origin.y) * rayLocal.invDirection.y
//
//    if ((tmin > tymax) || (tymin > tmax)) None
//    else {
//      if (tymin > tmin) tmin = tymin
//      if (tymax < tmax) tmax = tymax
//
//      val tzmin: Double = ((if (rayLocal.sign._3) bounds._2.z else bounds._1.z) - rayLocal.origin.z) * rayLocal.invDirection.z
//      val tzmax: Double = ((if (rayLocal.sign._3) bounds._1.z else bounds._2.z) - rayLocal.origin.z) * rayLocal.invDirection.z
//
//      if ((tmin > tzmax) || (tzmin > tmax)) None
//      else {
//        if (tzmin > tmin) tmin = tzmin
//        val pLocal: Point3d = rayLocal.origin + (rayLocal.direction * tmin)
//        val pWorld: Point3d = this.t transform pLocal
//        Option(RayResult(pLocal, pWorld, new Vector3d(0.0, 0.0, -1.0), Point2d.invalid, this.material, ray, tmin))
//      }
//    }
//  }
//
//  def intersectDistance(ray: Ray): Option[Double] = {
//    val rayLocal: Ray = this.t.transformInv(ray)
//
//    var tmin: Double = ((if (rayLocal.sign._1) bounds._2.x else bounds._1.x) - rayLocal.origin.x) * rayLocal.invDirection.x
//    var tmax: Double = ((if (rayLocal.sign._1) bounds._1.x else bounds._2.x) - rayLocal.origin.x) * rayLocal.invDirection.x
//
//    val tymin: Double = ((if (rayLocal.sign._2) bounds._2.y else bounds._1.y) - rayLocal.origin.y) * rayLocal.invDirection.y
//    val tymax: Double = ((if (rayLocal.sign._2) bounds._1.y else bounds._2.y) - rayLocal.origin.y) * rayLocal.invDirection.y
//
//    if ((tmin > tymax) || (tymin > tmax)) None
//    else {
//      if (tymin > tmin) tmin = tymin
//      if (tymax < tmax) tmax = tymax
//
//      val tzmin: Double = ((if (rayLocal.sign._3) bounds._2.z else bounds._1.z) - rayLocal.origin.z) * rayLocal.invDirection.z
//      val tzmax: Double = ((if (rayLocal.sign._3) bounds._1.z else bounds._2.z) - rayLocal.origin.z) * rayLocal.invDirection.z
//
//      if ((tmin > tzmax) || (tzmin > tmax)) None
//      else {
//        Option(if (tzmin > tmin) tzmin else tmin)
//      }
//    }
//  }
//
//  override val bbRadius = 1.0
//  override val boundingBox: AABBNode = AABBNode.leaf(bounds._1, bounds._2, this)
//}
