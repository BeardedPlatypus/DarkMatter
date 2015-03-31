package com.beardedplatypus.shading.brdf

import com.beardedplatypus.math.Vector3d
import com.beardedplatypus.shading.{Color, RayResult}

abstract class BRDF {
  def f(rayResult: RayResult, wi: Vector3d, wo: Vector3d): Color
  def sample_f(rayResult: RayResult, wi: Vector3d, wo: Vector3d): Color
  def rho(rayResult: RayResult, wo: Vector3d): Color
}