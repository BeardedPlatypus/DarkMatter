package com.beardedplatypus.shading.brdf

import com.beardedplatypus.math.{Cons, Vector3d}
import com.beardedplatypus.sampling.Sampler
import com.beardedplatypus.shading.{Texture, Color, RayResult}

class SpecularBRDF(val ks: Double, val t: Texture, val e: Double) extends BRDF {
  override def f(rayResult: RayResult, wi: Vector3d, wo: Vector3d): Color = t.colorAt(rayResult.uv) * (ks * Cons.invPI)
  override def rho(rayResult: RayResult, wo: Vector3d): Color = t.colorAt(rayResult.uv) * ks

  override def sampleColor(rayResult: RayResult, wo: Vector3d): Color = t.colorAt(rayResult.uv) * (ks * Cons.invPI)
  override def sampleBouncedRays(rayResult: RayResult, wo: Vector3d): (List[(Vector3d, Color, Double)]) = {
    val ndotwo = rayResult.normal dot wo
    val r = wo.inverted + rayResult.normal * (2.0 * ndotwo)

    // Orthonormal frame
    val w = r.normalized
    val u = (Vector3d(0.00424, 1.0, 0.00764) cross w).normalized
    val v = u cross w

    val ray = rayResult.ray
    (for (s <- Sampler.generateSamplesHemisphere(ray.branchingFactorRoot, ray.samplerStrategy, e)) yield {
      val precWi = u * s.x + v * s.y + w * s.z
      val wi = if ((rayResult.normal dot precWi) >= 0.0) precWi else u * -s.x + v * -s.y + w * s.z

      val phongLobe = Math.pow(r dot wi, e)
      val pdf = (rayResult.normal dot wi) * phongLobe

      (wi, t.colorAt(rayResult.uv) * (ks * phongLobe), pdf)
    }).toList
  }
}

object SpecularBRDF {
  def apply(kd: Double, cd: Color, e: Double): SpecularBRDF = new SpecularBRDF(kd, Texture.constant(cd), e)
  def apply(kd: Double, t: Texture, e: Double): SpecularBRDF = new SpecularBRDF(kd, t, e)
}