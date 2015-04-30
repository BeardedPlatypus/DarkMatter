package com.beardedplatypus.sampling

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

  private val rand: Random = new Random()
}

class Sample2d(val u: Double, val v: Double) {
  override def toString = "Sample2d | u: " + u + " v: " + v
}