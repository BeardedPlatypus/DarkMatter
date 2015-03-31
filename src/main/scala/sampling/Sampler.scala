package com.beardedplatypus.sampling

import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy

import scala.util.Random

object Sampler {
  def generateSamples(width: Int, height: Int,
                      offset_width: Int, offset_height: Int,
                      numSamplesRoot: Int, strategy: SamplerStrategy): IndexedSeq[Sample] = {
    strategy match {
      case SamplerStrategy.Constant => {
        val stepSize: Double = 1.0 / numSamplesRoot
        val stepOffset: Double = 0.5 * stepSize
        for (u <- offset_width to offset_width + width - 1;
             v <- offset_height to offset_height + height - 1;
             i <- 1 to numSamplesRoot;
             j <- 1 to numSamplesRoot) yield new Sample(u.toDouble + i.toDouble * stepSize - stepOffset,
                                                        v.toDouble + j.toDouble * stepSize - stepOffset,
                                                        u, v)
      }
      case SamplerStrategy.Jittered => {
        val stepSize: Double = 1.0 / numSamplesRoot
        for (u <- offset_width to offset_width + width - 1;
             v <- offset_height to offset_height + height - 1;
             i <- 0 to (numSamplesRoot - 1);
             j <- 0 to (numSamplesRoot - 1)) yield new Sample(u.toDouble + (i.toDouble + this.rand.nextDouble) * stepSize,
                                                              v.toDouble + (j.toDouble + this.rand.nextDouble) * stepSize,
                                                              u, v)
      }
      case SamplerStrategy.Random => {
        for (u <- offset_width to offset_width + width - 1;
             v <- offset_height to offset_height + height - 1;
             i <- 1 to (numSamplesRoot * numSamplesRoot)) yield new Sample(u.toDouble + this.rand.nextDouble(),
                                                                           v.toDouble + this.rand.nextDouble(),
                                                                           u, v)
      }
    }
  }

  private val rand: Random = new Random()
}

object SamplerStrategy extends Enumeration {
  type SamplerStrategy = Value
  val Jittered, Constant, Random = Value
}