package com.beardedplatypus.shading

import com.beardedplatypus.sampling.Sample
import com.beardedplatypus.world.{IntersectResult, Scene}

import scala.collection.parallel.immutable.ParSeq

object ShadingKernel {
  def evaluate(rayResults: ParSeq[IntersectResult], scene: Scene): ParSeq[ShadingResult] = (rayResults map ((x: IntersectResult) => new ShadingResult(x.sample,
                                                                                                                                                      if (x.hitResult.isEmpty) scene.backgroundColor
                                                                                                                                                      else ({x.hitResult.get.material.shade(x.hitResult.get, scene)})))).par
}

class ShadingResult(val sample: Sample,
                    val color: Color)