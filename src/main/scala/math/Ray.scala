package com.beardedplatypus.math

import com.beardedplatypus.sampling.{SamplerStrategy, Sample}
import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy


// TODO: Check whether normalization at the beginning is necessary
// TODO: find cleaner way to update this
class Ray(val origin: Point3d, val direction: Vector3d, val sample: Sample,
          val depth: Int, val branchingFactorRoot: Int, val samplerStrategy: SamplerStrategy) {
  val invDirection = Vector3d(1.0 / this.direction.x,
                              1.0 / this.direction.y,
                              1.0 / this.direction.z)
  val sign: (Boolean, Boolean, Boolean) = (invDirection.x < 0.0,
                                           invDirection.y < 0.0,
                                           invDirection.z < 0.0)

  override def toString: String = "Ray: origin: " + origin + " | dir: " + direction
}

object Ray {
  def apply(o: Point3d, d: Vector3d, s: Sample): Ray = new Ray(o, d, s, 0, 0, SamplerStrategy.Jittered)
  def apply(o: Point3d, d: Vector3d, s: Sample, bf: Int): Ray = new Ray(o, d, s, 0, bf, SamplerStrategy.Jittered)
  def apply(o: Point3d, dir: Vector3d, s: Sample,
            depth: Int, bf: Int, ss: SamplerStrategy) = new Ray(o, dir, s, depth, bf, ss)
}