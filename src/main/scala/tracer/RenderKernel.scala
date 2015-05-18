package com.beardedplatypus.tracer

import com.beardedplatypus.math.Ray
import com.beardedplatypus.shading.{Color, ShadingResult}
import com.beardedplatypus.world.{IntersectResult, Scene}


object RenderKernel {
  def getKernel(method: String): (IndexedSeq[Ray],Scene) => IndexedSeq[ShadingResult] = method match {
    case "direct" => evaluateDirect
    case "naive" => evaluateBruteForceGI
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

  def evaluateStepBruteForceGI(ray: Ray, scene: Scene): Color = {
    if (ray.depth > scene.maxDepthIndirect) Color.black
    else {
      val tRayResult = scene.intersectGeometry(ray)
      if (tRayResult.hitResult.isEmpty) scene.backgroundColor
      else {
        val rayResult = tRayResult.hitResult.get
        rayResult.material.shadePath(rayResult, scene)
      }
    }
  }
}
