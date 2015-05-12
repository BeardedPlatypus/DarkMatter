package com.beardedplatypus.sampling

import com.beardedplatypus.math.Point3d
import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy

import scala.util.Random

object SamplerStrategy extends Enumeration {
  type SamplerStrategy = Value
  val Jittered, Constant, Random = Value
}


object Sampler {
  def generateSamples(numSamplesRoot: Int,
                      strategy: SamplerStrategy): IndexedSeq[Sample2d] = {
    strategy match {
      case SamplerStrategy.Constant => {
        val stepSize: Double = 1.0 / numSamplesRoot
        val stepOffset: Double = 0.5 * stepSize
        for (i <- 0 to numSamplesRoot;
             j <- 0 to numSamplesRoot) yield new Sample2d(i * stepSize + stepOffset,
                                                          j * stepSize + stepOffset)
      }
      case SamplerStrategy.Jittered => {
        val stepSize: Double = 1.0 / numSamplesRoot
        for (i <- 0 to (numSamplesRoot - 1);
             j <- 0 to (numSamplesRoot - 1)) yield new Sample2d((i.toDouble + this.rand.nextDouble) * stepSize,
                                                                (j.toDouble + this.rand.nextDouble) * stepSize)
      }
      case SamplerStrategy.Random => {
        for (i <- 1 to (numSamplesRoot * numSamplesRoot)) yield new Sample2d(this.rand.nextDouble(),
                                                                             this.rand.nextDouble())
      }
    }
  }

  def generateSamplesHemisphere(numSamplesRoot: Int,
                                strategy: SamplerStrategy,
                                e: Double) = {
    for (s <- generateSamples(numSamplesRoot, strategy)) yield {
      val cosPhi = Math.cos(2.0 * Math.PI * s.u)
      val sinPhi = Math.sin(2.0 * Math.PI * s.u)

      val cosTheta = Math.pow(1.0 - s.v, 1.0 / (e + 1.0))
      val sinTheta = Math.sqrt(1.0 - cosTheta * cosTheta)

      val pu = sinTheta * cosPhi
      val pv = sinTheta * sinPhi
      val pw = cosTheta

      new Point3d(pu, pv, pw)
    }
  }

  private val rand: Random = new Random()
}

class Sample2d(val u: Double, val v: Double) {
  override def toString = "Sample2d | u: " + u + " v: " + v
}