package com.beardedplatypus.shading.material

import com.beardedplatypus.math.{Vector3d, Ray}
import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.shading.brdf.{GlossySpecularBRDF, LambertianBRDF}
import com.beardedplatypus.world.Scene
import com.beardedplatypus.world.light.Light

import scala.annotation.tailrec

class PhongMaterial(val ambientBRDF: LambertianBRDF,
                    val diffuseBRDF: LambertianBRDF,
                    val specularBRDF: GlossySpecularBRDF,
                    shadow: Boolean) extends Material(shadow) {
  override def shade(rayResult: RayResult,
                     scene: Scene): Color = {
    val wo: Vector3d = rayResult.ray.direction.inverted.normalized
    val ambientComp: Color = ambientBRDF.rho(rayResult, wo) * scene.ambientLight.L(rayResult.worldHitPoint)

    @tailrec
    def diffuseComponentShadow(lights: List[Light], acc: Color): Color = lights match {
      case Nil => acc
      case l :: ls => {
        val wi: Vector3d = l.direction(rayResult.worldHitPoint).normalized
        val ndotwi: Double = rayResult.normal dot wi
        val diffComp: Color = (diffuseBRDF.f(rayResult, wi, wo) +
                               specularBRDF.f(rayResult, wi, wo)) * l.L(rayResult.worldHitPoint) * ndotwi

        val shadowRay: Ray = Ray(rayResult.worldHitPoint, l.direction(rayResult.worldHitPoint), rayResult.ray.sample)
        
        if (ndotwi > 0.0 && (!this.receivesShadow || (!l.castsShadow || scene.visible(shadowRay, l.distanceTo(rayResult.worldHitPoint))))) {
          diffuseComponentShadow(ls, acc + diffComp)
        } else diffuseComponentShadow(ls, acc)
      }
    }

    ambientComp + diffuseComponentShadow(scene.lights, Color.black)
  }
}