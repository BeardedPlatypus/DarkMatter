package com.beardedplatypus.shading.material

import com.beardedplatypus.math.{Cons, Vector3d, Ray}
import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.shading.brdf.LambertianBRDF
import com.beardedplatypus.tracer.RenderKernel
import com.beardedplatypus.world.Scene
import com.beardedplatypus.world.light.Light
import sun.reflect.generics.reflectiveObjects.NotImplementedException
import scala.annotation.tailrec

class LambertMaterial(val ambientBRDF: LambertianBRDF,
                      val diffuseBRDF: LambertianBRDF,
                      shadow: Boolean) extends Material(shadow) {

  override def shade(rayResult: RayResult,
                     scene: Scene): Color = {
    val wo: Vector3d = rayResult.ray.direction.inverted
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
            diffuseContribution += diffuseBRDF.f(rayResult, s.direction, wo) *
                                   l.L(rayResult, s.direction) *
                                   l.G(rayResult.worldHitPoint, s.p) *
                                   ndotwi * l.pdfDiv(s.p)
          }
        }

        diffuseComponentShadow(ls, acc + diffuseContribution / samples.length.toDouble)
      }
    }

    ambientComp + diffuseComponentShadow(scene.lights, Color.black)
  }

  override def shadePath(rayResult: RayResult,
                         scene: Scene): Color = {
    val oldRay = rayResult.ray
    val wo = rayResult.ray.direction.inverted
    val color = this.diffuseBRDF.sampleColor(rayResult, wo)
    val normal = rayResult.normal
    @tailrec
    def calculateColourRecursive(sampledBounces: List[(Vector3d, Double)], colourAcc: Color): Color = sampledBounces match {
      case Nil => colourAcc / (oldRay.branchingFactorRoot * oldRay.branchingFactorRoot).toDouble
      case (wi, pdf) :: tail => {
        val nextRay = Ray(rayResult.worldHitPoint, wi, oldRay.sample,
                          oldRay.depth + 1, oldRay.branchingFactorRoot, oldRay.samplerStrategy)
        val colNextRay = RenderKernel.evaluateStepBruteForceGI(nextRay, scene)
        calculateColourRecursive(tail, colourAcc + (colNextRay * ((normal dot wi) / pdf)))
      }
    }

    color * calculateColourRecursive(this.diffuseBRDF.sampleBouncedRays(rayResult, wo), Color.black)
  }
}