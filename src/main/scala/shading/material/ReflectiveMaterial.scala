package com.beardedplatypus.shading.material

import com.beardedplatypus.math.Ray
import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.shading.brdf.PerfectSpecularBRDF
import com.beardedplatypus.tracer.RenderKernel
import com.beardedplatypus.world.Scene

class ReflectiveMaterial(val perfectSpecularBRDF: PerfectSpecularBRDF, shadow: Boolean) extends Material(shadow) {
  // TODO: Not implemented
  override def shade(rayResult: RayResult,
                     scene: Scene): Color = {
    Color.black
  }

  override def shadePath(rayResult: RayResult, scene: Scene): Color = {
    val wo = rayResult.ray.direction.inverted

    val (wi, f, pdf) = perfectSpecularBRDF.sampleBouncedRays(rayResult, wo).head
    val oldRay = rayResult.ray
    val reflectedRay = Ray(rayResult.worldHitPoint, wi,
                           oldRay.sample, oldRay.depth,
                           oldRay.branchingFactorRoot, oldRay.samplerStrategy, oldRay.russianRouletteFactor, oldRay.hasRussianRoulette)
    f * (RenderKernel.evaluateStepBruteForceGI(reflectedRay, scene) * ((rayResult.normal dot wi) / pdf))
  }

  override def shadePathWithDirect(rayResult: RayResult, scene: Scene): Color = {
    val wo = rayResult.ray.direction.inverted
    val f = perfectSpecularBRDF.sampleColor(rayResult, wo)

    val (wi, _, pdf) = perfectSpecularBRDF.sampleBouncedRays(rayResult, wo).head
    val oldRay = rayResult.ray
    val reflectedRay = Ray(rayResult.worldHitPoint, wi,
      oldRay.sample, oldRay.depth,
      oldRay.branchingFactorRoot, oldRay.samplerStrategy, oldRay.russianRouletteFactor, oldRay.hasRussianRoulette)
    f * (RenderKernel.evaluateStepHybridGI(reflectedRay, scene) * ((rayResult.normal dot wi) / pdf))// * reflectedRay.russianRouletteWeight))
  }
}