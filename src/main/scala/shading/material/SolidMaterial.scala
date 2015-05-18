package com.beardedplatypus.shading.material

import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.world.Scene

class SolidMaterial(val color: Color) extends Material(false) {
  override def shade(rayResult: RayResult, scene: Scene): Color = this.color
  override def shadePath(rayResult: RayResult, scene: Scene): Color = shade(rayResult, scene)
}