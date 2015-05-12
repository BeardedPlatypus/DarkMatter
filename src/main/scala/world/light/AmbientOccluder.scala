package com.beardedplatypus.world.light

import com.beardedplatypus.math.{Ray, Point3d, Vector3d}
import com.beardedplatypus.sampling.{Sample, Sampler}
import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy
import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.world.Scene
import com.beardedplatypus.world.geometry.acceleration_structures.AccelerationStructure

class AmbientOccluder(val color: Color, val strength: Double, val minAmount: Double,
                      val objects: AccelerationStructure,
                      val numSamplesRoot: Int,
                      val samplerStrategy: SamplerStrategy) extends Light {
  // TODO check whether feasible to use orthonormal basis class here
  override def L(rayResult: RayResult, direction: Vector3d): Color = {
    val w: Vector3d = rayResult.normal
    // Jitter vector in case normal is vertical
    val v: Vector3d = (w cross new Vector3d(0.0072, 1.0, 0.0034)).normalized
    val u: Vector3d = v cross w

    // FIXME needs more elegantness
    var occluded: Double = 0.0
    for(s <- Sampler.generateSamplesHemisphere(numSamplesRoot, samplerStrategy, 1)) {
      val direction = u * s.x  + v * s.y + w * s.z
      if (objects.isVisible(Ray(rayResult.worldHitPoint, direction, Sample.NaN), Double.PositiveInfinity)) occluded += 1
    }

    occluded /= (numSamplesRoot * numSamplesRoot)

    this.color * ((((1.0 - minAmount) * occluded) + minAmount) * strength)
  }

  override def sample(hitPoint: Point3d): List[LightSample] = throw new NotImplementedError()

  override val castsShadowLight: Boolean = false
}
