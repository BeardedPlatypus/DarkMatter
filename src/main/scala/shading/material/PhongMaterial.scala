package com.beardedplatypus.shading.material

import com.beardedplatypus.math.{Vector3d, Ray}
import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.shading.brdf.{GlossySpecularBRDF, LambertianBRDF}
import com.beardedplatypus.world.Scene
import com.beardedplatypus.world.light.{AreaLight, Light}

import scala.annotation.tailrec

class PhongMaterial(val ambientBRDF: LambertianBRDF,
                    val diffuseBRDF: LambertianBRDF,
                    val specularBRDF: GlossySpecularBRDF,
                    shadow: Boolean) extends Material(shadow) {
  override def shade(rayResult: RayResult,
                     scene: Scene): Color = {
    val wo: Vector3d = rayResult.ray.direction.inverted.normalized
    val ambientComp: Color = ambientBRDF.rho(rayResult, wo) * scene.ambientLight.L(rayResult, Vector3d.zero)

    @tailrec
    def diffuseComponentShadow(lights: List[Light], acc: Color): Color = lights match {
      case Nil => acc
      case l :: ls => {
        val samples = l sample rayResult.worldHitPoint
        var diffuseContribution = Color.black

        for (s <- samples) {
          val ndotwi: Double = rayResult.normal dot s.direction
          val shadowRay: Ray = Ray(rayResult.worldHitPoint, s.direction, rayResult.ray.sample)

          if (ndotwi > 0.0 && (!this.receivesShadow || !l.castsShadowLight || scene.visible(shadowRay, s.distance))) {
            diffuseContribution += (diffuseBRDF.f(rayResult, s.direction, wo) +
                                    specularBRDF.f(rayResult, s.direction, wo)) *
                                   l.L(rayResult, s.direction) *
                                   l.G(rayResult.worldHitPoint, s.p) *
                                   ndotwi * l.pdfDiv(s.p)
          }
        }
        diffuseComponentShadow(ls, acc + diffuseContribution  / samples.length.toDouble )
      }
    }
    ambientComp + diffuseComponentShadow(scene.lights, Color.black)
  }
}