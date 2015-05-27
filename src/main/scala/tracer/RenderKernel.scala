package com.beardedplatypus.tracer

import com.beardedplatypus.math.Ray
import com.beardedplatypus.shading.{Color, ShadingResult}
import com.beardedplatypus.world.{IntersectResult, Scene}

import scala.util.Random


object RenderKernel {
  def getKernel(method: String): (IndexedSeq[Ray],Scene) => IndexedSeq[ShadingResult] = method match {
    case "direct" => evaluateDirect
    case "naive" => evaluateBruteForceGI
    case "hybrid" => evaluateHybridGI
    case _ => throw new IllegalArgumentException("Specified kernel does not exist.")
  }

  val evaluateDirect: (IndexedSeq[Ray],Scene) => IndexedSeq[ShadingResult] = {
    (primaryRays: IndexedSeq[Ray], scene: Scene) => {
      // calculate the hit points
      val rayResults = primaryRays map scene.intersectGeometry
      // calculate the shaded results
      rayResults map ((x: IntersectResult) => {
        new ShadingResult(x.sample, if (x.hitResult.isEmpty) scene.backgroundColor
                                    else x.hitResult.get.material.shade(x.hitResult.get, scene))
      })
    }
  }

  val evaluateBruteForceGI: (IndexedSeq[Ray],Scene) => IndexedSeq[ShadingResult] = {
    (primaryRays: IndexedSeq[Ray], scene: Scene) => {
      // calculate the hit points
      for (ray <- primaryRays) yield {
        new ShadingResult(ray.sample, evaluateStepBruteForceGI(ray, scene))
      }
    }
  }

  val evaluateHybridGI: (IndexedSeq[Ray],Scene) => IndexedSeq[ShadingResult] = {
    (primaryRays: IndexedSeq[Ray], scene: Scene) => {
      for (ray <- primaryRays) yield {
        new ShadingResult(ray.sample, evaluateStepHybridGI(ray, scene))
      }
    }
  }

  def evaluateStepBruteForceGI(ray: Ray, scene: Scene): Color = {
    if (ray.depth > scene.maxDepthIndirect) Color.black
    else {
      if (ray.hasRussianRoulette && ray.depth > 0) {
        if (rand.nextDouble() > ray.russianRouletteFactor) return Color.black
      }
      val tRayResult = scene.intersectGeometry(ray)
      if (tRayResult.hitResult.isEmpty) scene.backgroundColor * (if (ray.depth > 0) ray.russianRouletteWeight else 1.0)
      else {
        val rayResult = tRayResult.hitResult.get
        rayResult.material.shadePath(rayResult, scene) * (if (ray.depth > 0) ray.russianRouletteWeight else 1.0)
      }
    }
  }

  def evaluateStepHybridGI(ray: Ray, scene: Scene): Color = {
    if (ray.depth > scene.maxDepthIndirect) Color.black
    else {
      if (ray.hasRussianRoulette && ray.depth > 0) {
        if (rand.nextDouble() > ray.russianRouletteFactor) return Color.black
      }
      val tRayResult = scene.intersectGeometry(ray)
      if (tRayResult.hitResult.isEmpty) scene.backgroundColor * (if (ray.depth > 0) ray.russianRouletteWeight else 1.0)
      else {
        val rayResult = tRayResult.hitResult.get
        if (ray.depth > scene.maxDepthDirect) rayResult.material.shadePath(rayResult, scene) * (if (ray.depth > 0) ray.russianRouletteWeight else 1.0)
        else rayResult.material.shadePathWithDirect(rayResult, scene) * (if (ray.depth > 0) ray.russianRouletteWeight else 1.0)
      }
    }
  }

  private val rand: Random = new Random()
}
