package com.beardedplatypus.shading

import com.beardedplatypus.math.{Point2d, Vector3d, Ray, Point3d}
import com.beardedplatypus.shading.material.Material

class RayResult(val localHitPoint: Point3d,
                val worldHitPoint: Point3d,
                val normal: Vector3d,
                val uv: Point2d,
                val material: Material,
                val ray: Ray,
                val distance: Double) {
}

object RayResult {
  def apply(lh: Point3d,
            wh: Point3d,
            n: Vector3d,
            uv: Point2d,
            m: Material,
            r: Ray,
            d: Double): RayResult = {
    new RayResult(lh, wh, n, uv, m, r, d)
  }
}

class RayResultLocal(val localHitpoint: Point3d,
                      val localNormal : Vector3d,
                      val uv: Point2d,
                      val distance: Double)

object RayResultLocal {
  def apply(localHitpoint: Point3d,
            localNormal: Vector3d,
            uv: Point2d,
            distance: Double): RayResultLocal = {
    new RayResultLocal(localHitpoint, localNormal, uv, distance)
  }
}