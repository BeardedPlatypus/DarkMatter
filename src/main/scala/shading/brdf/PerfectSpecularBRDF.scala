package com.beardedplatypus.shading.brdf

import com.beardedplatypus.math.Vector3d
import com.beardedplatypus.shading.{Color, RayResult, Texture}

class PerfectSpecularBRDF(val kr: Double, val t: Texture) extends BRDF {
  override def f(rayResult: RayResult, wi: Vector3d, wo: Vector3d): Color = t.colorAt(rayResult.uv) * kr
  override def rho(rayResult: RayResult, wo: Vector3d): Color = t.colorAt(rayResult.uv) * kr

  override def sampleColor(rayResult: RayResult, wo: Vector3d): Color = t.colorAt(rayResult.uv) * kr
  override def sampleBouncedRays(rayResult: RayResult, wo: Vector3d): (List[(Vector3d, Color, Double)]) = {
    val ndotwo = rayResult.normal dot wo
    val wi = wo.inverted + rayResult.normal * (2.0 * ndotwo)

    List((wi, t.colorAt(rayResult.uv) * kr, rayResult.normal dot wi))
  }
}

object PerfectSpecularBRDF {
  def apply(kr: Double, cd: Color): PerfectSpecularBRDF = new PerfectSpecularBRDF(kr, Texture.constant(cd))
  def apply(kr: Double, t: Texture): PerfectSpecularBRDF = new PerfectSpecularBRDF(kr, t)
}
