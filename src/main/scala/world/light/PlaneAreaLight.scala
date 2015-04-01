package com.beardedplatypus.world.light

import com.beardedplatypus.math.{Point3d, Vector3d, Transformation}
import com.beardedplatypus.shading.material.EmissiveMaterial
import com.beardedplatypus.world.geometry.objects.primitives.FinitePlane

class PlaneAreaLight(transformation: Transformation,
                     val emissiveMaterial: EmissiveMaterial,
                     castShadow: Boolean) extends FinitePlane(transformation=transformation,
                                                              material=emissiveMaterial,
                                                              castShadow=castShadow)
                                          with AreaLight {
}
