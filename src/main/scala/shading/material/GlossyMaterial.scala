package com.beardedplatypus.shading.material

import com.beardedplatypus.math.{Vector3d, Ray}
import com.beardedplatypus.shading.brdf.SpecularBRDF
import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.tracer.RenderKernel
import com.beardedplatypus.world.Scene

import scala.annotation.tailrec

class GlossyMaterial(val specularBRDF: SpecularBRDF ,shadow: Boolean) extends Material(shadow) {
  // TODO: Not implemented
  override def shade(rayResult: RayResult,
                     scene: Scene): Color = {
    Color.black
  }

  override def shadePath(rayResult: RayResult, scene: Scene): Color = {
    val oldRay = rayResult.ray
    val normal = rayResult.normal

    @tailrec
    def computeColorRecursive(sampledBounces: List[(Vector3d, Color, Double)], colourAcc: Color): Color = sampledBounces match {
      case Nil => colourAcc / (oldRay.branchingFactorRoot * oldRay.branchingFactorRoot).toDouble
      case (wi, c, pdf) :: tail => {
        val nextRay = Ray(rayResult.worldHitPoint, wi, oldRay.sample,
          oldRay.depth, oldRay.branchingFactorRoot, oldRay.samplerStrategy, oldRay.russianRouletteFactor, oldRay.hasRussianRoulette)
        val colNextRay = RenderKernel.evaluateStepBruteForceGI(nextRay, scene)
        computeColorRecursive(tail, colourAcc + (colNextRay * c * ((normal dot wi) / pdf)))
      }
    }

    val wo = oldRay.direction.inverted
    computeColorRecursive(specularBRDF.sampleBouncedRays(rayResult, wo), Color.black)
  }

  override def shadePathWithDirect(rayResult: RayResult, scene: Scene): Color = {
    val oldRay = rayResult.ray
    val normal = rayResult.normal

    @tailrec
    def computeColorRecursive(sampledBounces: List[(Vector3d, Color, Double)], colourAcc: Color): Color = sampledBounces match {
      case Nil => colourAcc / (oldRay.branchingFactorRoot * oldRay.branchingFactorRoot).toDouble
      case (wi, c, pdf) :: tail => {
        val nextRay = Ray(rayResult.worldHitPoint, wi, oldRay.sample,
          oldRay.depth, oldRay.branchingFactorRoot, oldRay.samplerStrategy, oldRay.russianRouletteFactor, oldRay.hasRussianRoulette)
        val colNextRay = RenderKernel.evaluateStepHybridGI(nextRay, scene)
        computeColorRecursive(tail, colourAcc + (colNextRay * c * ((normal dot wi) / pdf)))
      }
    }

    val wo = oldRay.direction.inverted
    computeColorRecursive(specularBRDF.sampleBouncedRays(rayResult, wo), Color.black)
  }
}
