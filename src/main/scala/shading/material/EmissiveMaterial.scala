package com.beardedplatypus.shading.material

import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.world.Scene

class EmissiveMaterial(val ls: Double,
                       val c: Color) extends Material(false) {


  override def shade(rayResult: RayResult, scene: Scene): Color  = {
    if ((rayResult.normal dot rayResult.ray.direction.inverted) >= 0.0) c * ls
    else Color.black
  }
}