package com.beardedplatypus.shading.material

import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.world.Scene
import sun.reflect.generics.reflectiveObjects.NotImplementedException

class EmissiveMaterial(val ls: Double,
                       val c: Color,
                       val isDirect: Boolean) extends Material(false) {
  val Le = c * ls

  override def shade(rayResult: RayResult, scene: Scene): Color  = {
    if ((rayResult.normal dot rayResult.ray.direction.inverted) >= 0.0) Le
    else Color.black
  }

  override def shadePath(rayResult: RayResult, scene: Scene): Color = {
    if ((rayResult.normal dot rayResult.ray.direction.inverted) >= 0.0) Le
    else Color.black
  }

  override def shadePathWithDirect(rayResult: RayResult, scene: Scene): Color = {
    if ((!isDirect || rayResult.ray.depth == 0) && (rayResult.normal dot rayResult.ray.direction.inverted) >= 0.0) Le
    else Color.black
  }
}