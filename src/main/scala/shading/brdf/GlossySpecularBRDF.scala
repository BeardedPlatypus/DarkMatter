package com.beardedplatypus.shading.brdf

import com.beardedplatypus.math.Vector3d
import com.beardedplatypus.sampling.SamplerStrategy._
import com.beardedplatypus.shading.{Color, RayResult}

class GlossySpecularBRDF(val ks: Double, val cs: Color, exponent: Double) extends BRDF {
  override def f(rayResult: RayResult, wi: Vector3d, wo: Vector3d): Color = {
    val n: Vector3d = rayResult.normal
    val r: Vector3d = (n * (2.0 * (n dot wi))) + (wi * -1.0)
    val r_dot_wo: Double = r dot wo

    if (r_dot_wo > 0.0) cs * (ks * Math.pow(r_dot_wo, exponent)) else Color.black
  }

  override def rho(rayResult: RayResult, wo: Vector3d): Color = Color.black

  def sampleColor(rayResult: RayResult, wo: Vector3d): Color = Color.black //TODO
  def sampleBouncedRays(rayResult: RayResult, wi: Vector3d): (List[(Vector3d, Color, Double)]) = Nil
}