package com.beardedplatypus.shading.material

import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.world.Scene
import sun.reflect.generics.reflectiveObjects.NotImplementedException

class NormalMaterial extends Material(false) {
  override def shade(rayResult: RayResult, scene: Scene): Color = Color(0.5 + rayResult.normal.x * 0.5,
                                                                        0.5 + rayResult.normal.y * 0.5,
                                                                        0.5 - rayResult.normal.z * 0.5)

  override def shadePath(rayResult: RayResult, scene: Scene): Color = shade(rayResult, scene)
}

class AbsNormalMaterial extends Material(false) {
  override def shade(rayResult: RayResult, scene: Scene): Color = Color(Math.abs(rayResult.normal.x),
    Math.abs(rayResult.normal.y),
    Math.abs(rayResult.normal.z))

  override def shadePath(rayResult: RayResult, scene: Scene): Color = shade(rayResult, scene)
}