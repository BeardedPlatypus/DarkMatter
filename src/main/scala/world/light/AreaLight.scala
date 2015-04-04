package com.beardedplatypus.world.light

import com.beardedplatypus.shading.material.EmissiveMaterial

trait   AreaLight extends Light {
  val emissiveMaterial: EmissiveMaterial
}