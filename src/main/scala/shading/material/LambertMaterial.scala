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
    def calculateColourRecursive(sampledBounces: List[(Vector3d, Color, Double)], colourAcc: Color): Color = sampledBounces match {
      case Nil => colourAcc / (oldRay.branchingFactorRoot * oldRay.branchingFactorRoot).toDouble
      case (wi, _, pdf) :: tail => {
        val nextRay = Ray(rayResult.worldHitPoint, wi, oldRay.sample,
                          oldRay.depth + 1, oldRay.branchingFactorRoot, oldRay.samplerStrategy, oldRay.russianRouletteFactor, oldRay.hasRussianRoulette)
        val colNextRay = RenderKernel.evaluateStepBruteForceGI(nextRay, scene)
        calculateColourRecursive(tail, colourAcc + (colNextRay * ((normal dot wi) / pdf)))
      }
    }

    color * calculateColourRecursive(this.diffuseBRDF.sampleBouncedRays(rayResult, wo), Color.black)
  }

  override def shadePathWithDirect(rayResult: RayResult, scene: Scene): Color = {
    val oldRay = rayResult.ray
    val wo = rayResult.ray.direction.inverted
    val color = this.diffuseBRDF.sampleColor(rayResult, wo)
    val normal = rayResult.normal

    // Direct lighting calculation
    @tailrec
    def directDiffuseComponent(lights: List[Light], acc: Color): Color = lights match {
      case Nil => acc
      case l :: ls => {
        var diffuseContribution = Color.black

        val samples = l sample rayResult.worldHitPoint
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

        directDiffuseComponent(ls, acc + diffuseContribution / samples.length.toDouble)
      }
    }

    // Indirict lighting calculation
    @tailrec
    def indirectDiffuseComponent(sampledBounces: List[(Vector3d, Color, Double)], colourAcc: Color): Color = sampledBounces match {
      case Nil => colourAcc / (oldRay.branchingFactorRoot * oldRay.branchingFactorRoot).toDouble
      case (wi, _, pdf) :: tail => {
        val nextRay = Ray(rayResult.worldHitPoint, wi, oldRay.sample,
          oldRay.depth + 1, oldRay.branchingFactorRoot, oldRay.samplerStrategy, oldRay.russianRouletteFactor, oldRay.hasRussianRoulette)
        val colNextRay = RenderKernel.evaluateStepHybridGI(nextRay, scene)
        indirectDiffuseComponent(tail, colourAcc + (colNextRay * ((normal dot wi) / pdf)))
      }
    }

    // Combination
    directDiffuseComponent(scene.lights, Color.black) +
      color * indirectDiffuseComponent(this.diffuseBRDF.sampleBouncedRays(rayResult, wo), Color.black)
  }
}