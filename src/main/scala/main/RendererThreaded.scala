package com.beardedplatypus.main

import com.beardedplatypus.math.Ray
import com.beardedplatypus.shading.{ShadingResult, ColorRGB, Color}
import com.beardedplatypus.world.Scene
import com.beardedplatypus.sampling.{Sample2d, Sample, Sampler, SamplerStrategy}
import SamplerStrategy.SamplerStrategy
import gui.{RenderFrame, ImagePanel}

import main.ConfigParser

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global


object RendererThreaded {
  def main(args: Array[String]): Unit = {
    val renderSettings: RenderSettings = ConfigParser.parse("..\\settings.ini")
    val renderer = new RenderManager(renderSettings)
    renderer.render
  }
}


class RenderManager(renderSettings: RenderSettings) {
  def render: Unit = {
    val panel: ImagePanel = new ImagePanel(renderSettings.width, renderSettings.height)
    val frame: RenderFrame = new RenderFrame(renderSettings.name, panel)

    def updateFrame(bucketResult: BucketResult) = {
      val pixelIndexX = renderSettings.bucketSize._1 * bucketResult.pos._1
      val pixelIndexY = renderSettings.bucketSize._2 * bucketResult.pos._2

      var color: ColorRGB = null
      val pixelColors = bucketResult.pixelColors
      for (y <- 0 to renderSettings.bucketSize._2 - 1; x <- 0 to renderSettings.bucketSize._1 - 1) {
        color = pixelColors(y)(x).toRGB
        panel.set(pixelIndexX + x, pixelIndexY + y, 255, color.r, color.g, color.b)
      }
    }

    for (u <- 0 to (renderSettings.width / renderSettings.bucketSize._1) - 1;
         v <- 0 to (renderSettings.height / renderSettings.bucketSize._2) - 1) {
      Future {
        renderBucket(new BucketSettings(pos = (u, v),
          size = renderSettings.bucketSize,
          kernel = renderSettings.kernel,
          scene = renderSettings.scene,
          samplesRootAA = renderSettings.samplesRootAA,
          samplerStrategyAA = renderSettings.samplerStrategyAA,
          branchingFactor = renderSettings.branchingFactor,
          samplerStrategyGI = renderSettings.samplerStrategyGI,
          hasRussianRoulette = renderSettings.hasRussianRoulette,
          russianRouletteFactor = renderSettings.russianRouletteFactor))
      }.mapTo[BucketResult] onSuccess {
        case result => updateFrame(result)
      }
    }
  }

  def renderBucket(bucketSettings: BucketSettings): BucketResult = {
    val scene = bucketSettings.scene
    val offsetX = bucketSettings.pos._1 * bucketSettings.size._1
    val offsetY = bucketSettings.pos._2 * bucketSettings.size._2

    val rays = if (bucketSettings.hasRussianRoulette) {
      for (s <- renderSamples(bucketSettings, offsetX, offsetY)) yield {
        scene.camera.generatePrimaryRay(s, bucketSettings.branchingFactor, bucketSettings.samplerStrategyGI, bucketSettings.russianRouletteFactor)
      }
    } else {
      for (s <- renderSamples(bucketSettings, offsetX, offsetY)) yield {
        scene.camera.generatePrimaryRay(s, bucketSettings.branchingFactor, bucketSettings.samplerStrategyGI)
      }
    }
    val pixels = bucketSettings.kernel(rays, scene)

    val res: Array[Array[Color]] = Array.ofDim(bucketSettings.size._1, bucketSettings.size._2)
    var pixelsConsume = pixels

    if (bucketSettings.samplesPerPixelAA > 1) {
      val pixelList = pixels.toList
      val div: Double = 1.0 / bucketSettings.samplesPerPixelAA
      for (x <- 0 to bucketSettings.size._1 - 1; y <- 0 to bucketSettings.size._2 - 1) res(y)(x) = Color.black
      for (p <- pixelList) res(p.sample.v - offsetY)(p.sample.u - offsetX) += p.color
      for (x <- 0 to bucketSettings.size._1 - 1; y <- 0 to bucketSettings.size._2 - 1) res(y)(x) *= div
    } else {
      for (x <- 0 to bucketSettings.size._1 - 1; y <- 0 to bucketSettings.size._2 - 1) {
        res(y)(x) = pixelsConsume.head.color
        pixelsConsume = pixelsConsume.tail
      }
    }
    new BucketResult(bucketSettings.pos, res)
  }

  def renderSamples(bucketSettings: BucketSettings,
                    offsetX: Int, offsetY: Int): IndexedSeq[Sample] = {


    val samples = for (u <- offsetX to offsetX + bucketSettings.size._1 - 1;
                       v <- offsetY to offsetY + bucketSettings.size._2 - 1) yield {
      val samplesPixel = Sampler.generateSamples(bucketSettings.samplesRootAA, bucketSettings.samplerStrategyAA)
      samplesPixel map ((s: Sample2d) => new Sample(u.toDouble + s.u, v.toDouble + s.v, u, v))
    }
    samples.flatten
  }
}


class BucketResult(val pos: (Int, Int),
                   val pixelColors: Array[Array[Color]])


class RenderSettings(val name: String,
                     val bucketSize: (Int, Int),
                     val kernel: (IndexedSeq[Ray],Scene) => IndexedSeq[ShadingResult],
                     val scene: Scene,
                     val samplesRootAA: Int,
                     val samplerStrategyAA: SamplerStrategy,
                     val branchingFactor: Int,
                     val samplerStrategyGI: SamplerStrategy,
                     val hasRussianRoulette: Boolean,
                     val russianRouletteFactor: Double) {
  // TODO: fix this dynamically in render collector
  val width = scene.camera.xRes
  val height = scene.camera.yRes

  require(width%bucketSize._1 == 0)
  require(height%bucketSize._2 == 0)
}

class BucketSettings(val pos: (Int, Int),
                     val size: (Int, Int),
                     val kernel: (IndexedSeq[Ray],Scene) => IndexedSeq[ShadingResult],
                     val scene: Scene,
                     val samplesRootAA: Int,
                     val samplerStrategyAA: SamplerStrategy,
                     val branchingFactor: Int,
                     val samplerStrategyGI: SamplerStrategy,
                     val hasRussianRoulette: Boolean,
                     val russianRouletteFactor: Double) {
  val samplesPerPixelAA = samplesRootAA * samplesRootAA
}