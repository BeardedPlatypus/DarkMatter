package com.beardedplatypus.shading.brdf

import com.beardedplatypus.math.{Vector3d, Cons}
import com.beardedplatypus.shading.{Color, RayResult, Texture}

class LambertianBRDF(val kd: Double, val t: Texture) extends BRDF{

  override def f(rayResult: RayResult, wi: Vector3d, wo: Vector3d): Color = t.colorAt(rayResult.uv) * (kd * Cons.invPI)
  override def sample_f(rayResult: RayResult, wi: Vector3d, wo: Vector3d): Color = Color.black //TODO
  override def rho(rayResult: RayResult, wo: Vector3d): Color = t.colorAt(rayResult.uv) * kd
}

object LambertianBRDF {
  def apply(kd: Double, cd: Color): LambertianBRDF = new LambertianBRDF(kd, Texture.constant(cd))
  def apply(kd: Double, t: Texture): LambertianBRDF = new LambertianBRDF(kd, t)
}