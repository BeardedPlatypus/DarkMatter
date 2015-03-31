package com.beardedplatypus.math

import com.beardedplatypus.sampling.Sample


// TODO: Check whether normalization at the beginning is necessary
// TODO: find cleaner way to update this
class Ray(val origin: Point3d, val direction: Vector3d, val sample: Sample) {

  // AABB VH optimization code.
  val invDirection = Vector3d(1.0 / this.direction.x,
                              1.0 / this.direction.y,
                              1.0 / this.direction.z)
  val sign: (Boolean, Boolean, Boolean) = (invDirection.x < 0.0,
                                           invDirection.y < 0.0,
                                           invDirection.z < 0.0)
//  // -------------------------------------------------------------------------
//  // Slopes
//  // Unused due to different bounding box implementation.
//  lazy val s_yx = direction.x * invDirection.y
//  lazy val s_xy = direction.y * invDirection.x
//
//  lazy val s_zy = direction.y * invDirection.z
//  lazy val s_yz = direction.z * invDirection.y
//
//  lazy val s_xz = direction.x * invDirection.z
//  lazy val s_zx = direction.z * invDirection.x
//
//  // -------------------------------------------------------------------------
//  // Pre-computation
//  lazy val c_xy = origin.y - s_xy * origin.x
//  lazy val c_yx = origin.x - s_yx * origin.y
//
//  lazy val c_zy = origin.y - s_zy * origin.z
//  lazy val c_yz = origin.z - s_yz * origin.y
//
//  lazy val c_xz = origin.z - s_xz * origin.x
//  lazy val c_zx = origin.x - s_zx * origin.z

  override def toString: String = "Ray: origin: " + origin + " | dir: " + direction
}

object Ray {
  def apply(o: Point3d, d: Vector3d, s: Sample): Ray = new Ray(o, d, s)
}