package com.beardedplatypus.shading.material

import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.world.Scene

abstract class Material(val receivesShadow: Boolean) {
  def shade(rayResult: RayResult, scene: Scene): Color
}