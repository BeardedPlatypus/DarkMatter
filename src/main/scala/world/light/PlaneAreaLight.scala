package com.beardedplatypus.world.light

import com.beardedplatypus.math.{Cons, Point3d, Vector3d, Transformation}
import com.beardedplatypus.sampling.SamplerStrategy
import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy
import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy
import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.shading.material.EmissiveMaterial
import com.beardedplatypus.world.geometry.objects.primitives.FinitePlane

import scala.util.Random

class PlaneAreaLight(transformation: Transformation,
                     val emissiveMaterial: EmissiveMaterial,
                     val numSamplesRoot: Int,
                     val samplerStrategy: SamplerStrategy,
                     override val castsShadowLight: Boolean) extends FinitePlane(transformation=transformation,
                                                              material=emissiveMaterial,
                                                              castShadow=false)
                                          with AreaLight {
  protected val normalInv = transformation.transform(normalLocal).inverted

  override def L(rayResult: RayResult, wi: Vector3d): Color = {
    val ndotd: Double = normalInv dot wi

    if (ndotd > - Cons.kEpsilon) emissiveMaterial.Le
    else Color.black
  }

  override def G(pHit: Point3d, pSample: Point3d): Double = {
    val wi = pSample - pHit
    val ndotd: Double = normalInv dot wi
    ndotd * squaredDDiv
  }

  override def pdfDiv(pSample: Point3d) = squaredD

  private val squaredD: Double = {
    val p1 = transformation transform new Point3d(-0.5, 0.0, -0.5)
    val p2 = transformation transform new Point3d(-0.5, 0.0, 0.5)
    val p3 = transformation transform new Point3d(0.5, 0.0, -0.5)

    val vec12 = p2 - p1
    val vec13 = p3 - p1

    Math.sqrt(vec12.normSq) * Math.sqrt(vec13.normSq)
  }

  private val squaredDDiv: Double = 1.0 / squaredD

  // FIXME: Fix the improper distribution when object is scaled.

  val stepSize: Double = 1.0 / numSamplesRoot.toDouble
  private val rand: Random = new Random()

  override def sample(hitPoint: Point3d): List[LightSample] = {
    samplerStrategy match {
      case SamplerStrategy.Constant => {
        for (i <- 0 to numSamplesRoot - 1; j <- 0 to numSamplesRoot) yield {
          val pSample = transformation transform (new Point3d( (i.toDouble + 0.5) * stepSize -0.5, 0.0, (j.toDouble + 0.5) * stepSize - 0.5))
          new LightSample(pSample, direction(hitPoint, pSample), distanceTo(hitPoint, pSample))
        }
      }.to
      case SamplerStrategy.Jittered => {
        for (i <- 0 to numSamplesRoot - 1; j <- 0 to numSamplesRoot) yield {
          val pSample = transformation transform (new Point3d((i.toDouble + rand.nextDouble()) * stepSize - 0.5,
                                                              0.0,
                                                              (j.toDouble + rand.nextDouble()) * stepSize - 0.5))
          new LightSample(pSample, direction(hitPoint, pSample), distanceTo(hitPoint, pSample))
        }
      }.to
      case SamplerStrategy.Random => {
        for (i <- 0 to numSamplesRoot - 1; j <- 0 to numSamplesRoot) yield {
          val pSample = transformation transform (new Point3d(rand.nextDouble() - 0.5, 0.0, rand.nextDouble() - 0.5))
          new LightSample(pSample, direction(hitPoint, pSample), distanceTo(hitPoint, pSample))
        }
      }.to
    }
  }

  private def direction(hitPoint: Point3d, pSample: Point3d): Vector3d = (pSample - hitPoint).normalized
  private def distanceTo(hitPoint: Point3d, pSample: Point3d): Double =  {
    val p: Vector3d = hitPoint - pSample
    Math.sqrt(p dot p)
  }

}
