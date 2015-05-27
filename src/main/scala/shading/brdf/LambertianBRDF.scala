package com.beardedplatypus.shading.brdf

import com.beardedplatypus.math.{Vector3d, Cons}
import com.beardedplatypus.sampling.Sampler
import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy
import com.beardedplatypus.shading.{Color, RayResult, Texture}

class LambertianBRDF(val kd: Double, val t: Texture) extends BRDF{
  override def f(rayResult: RayResult, wi: Vector3d, wo: Vector3d): Color = t.colorAt(rayResult.uv) * (kd * Cons.invPI)
  override def rho(rayResult: RayResult, wo: Vector3d): Color = t.colorAt(rayResult.uv) * kd

  override def sampleColor(rayResult: RayResult, wo: Vector3d): Color = t.colorAt(rayResult.uv) * (kd * Cons.invPI)
  override def sampleBouncedRays(rayResult: RayResult, wo: Vector3d): (List[(Vector3d, Color, Double)]) = {
    // Orthonormal frame
    val w = rayResult.normal
    val v = (Vector3d(0.0034, 1.0, 0.0071) cross w).normalized
    val u = v cross w

    val color = t.colorAt(rayResult.uv) * (kd * Cons.invPI)
    val ray = rayResult.ray
    (for (s <- Sampler.generateSamplesHemisphere(ray.branchingFactorRoot, ray.samplerStrategy, 1)) yield {
      val wi = u * s.x + v * s.y + w * s.z
      val pdf = (w dot wi) * Cons.invPI
      (wi, color, pdf)
    }).toList
  }
}

object LambertianBRDF {
  def apply(kd: Double, cd: Color): LambertianBRDF = new LambertianBRDF(kd, Texture.constant(cd))
  def apply(kd: Double, t: Texture): LambertianBRDF = new LambertianBRDF(kd, t)
}