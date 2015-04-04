package com.beardedplatypus.main

import com.beardedplatypus.shading.{ShadingKernel, ColorRGB, Color}
import com.beardedplatypus.world.Scene
import com.beardedplatypus.sampling.{Sampler, SamplerStrategy}
import SamplerStrategy.SamplerStrategy
import gui.{RenderFrame, ImagePanel}

import java.io.File
import javax.imageio.ImageIO

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global


object RendererThreaded {
  def main(args: Array[String]): Unit = {
    val (width, height) = (640, 640)

    // Scene reading
    val scene = SceneBuilder.sceneAreaLightTest2(width, height)

    // Render delegation.
    val renderSettings: RenderSettings = new RenderSettings(name = "test render",
                                                            bucketSize = (4, 4),
                                                            scene = scene,
                                                            samplesRoot = 4,
                                                            samplerStrategy = SamplerStrategy.Jittered)
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
          scene = renderSettings.scene,
          samplesRoot = renderSettings.samplesRoot,
          samplerStrategy = renderSettings.samplerStrategy))
      }.mapTo[BucketResult] onSuccess {
        case result => updateFrame(result)
      }
    }
  }

  def renderBucket(bucketSettings: BucketSettings): BucketResult = {
    val scene = bucketSettings.scene
    val offsetX = bucketSettings.pos._1 * bucketSettings.size._1
    val offsetY = bucketSettings.pos._2 * bucketSettings.size._2

    val samples = Sampler.generateSamples(bucketSettings.size._1,
                                          bucketSettings.size._2,
                                          offsetX,
                                          offsetY,
                                          bucketSettings.samplesRoot,
                                          bucketSettings.samplerStrategy)

    val rays = for (s <- samples) yield scene.camera.generateRay(s)
    val hits = (rays map scene.intersectGeometry).par
    val pixels = ShadingKernel evaluate(hits.to, scene)  // FIXME: changes hits.to

    val res: Array[Array[Color]] = Array.ofDim(bucketSettings.size._1, bucketSettings.size._2)
    var pixelsConsume = pixels

    if (bucketSettings.samplesPerPixel > 1) {
      val pixelList = pixels.toList
      val div: Double = 1.0 / bucketSettings.samplesPerPixel
      for (x <- 0 to bucketSettings.size._1 - 1; y <- 0 to bucketSettings.size._2 - 1) res(y)(x) = Color.black
      for (p <- pixelList) res(p.sample.v - offsetY)(p.sample.u - offsetX) += p.color
      for (x <- 0 to bucketSettings.size._1 - 1; y <- 0 to bucketSettings.size._2 - 1) res(y)(x) = res(y)(x) * div
    } else {
      for (x <- 0 to bucketSettings.size._1 - 1; y <- 0 to bucketSettings.size._2 - 1) {
        res(y)(x) = pixelsConsume.head.color
        pixelsConsume = pixelsConsume.tail
      }
    }
    new BucketResult(bucketSettings.pos, res)
  }
}


class BucketResult(val pos: (Int, Int),
                   val pixelColors: Array[Array[Color]])

class RenderSettings(val name: String,
                     val bucketSize: (Int, Int),
                     val scene: Scene,
                     val samplesRoot: Int,
                     val samplerStrategy: SamplerStrategy) {
  // TODO: fix this dynamically in render collector
  val width = scene.camera.xRes
  val height = scene.camera.yRes

  require(width%bucketSize._1 == 0)
  require(height%bucketSize._2 == 0)
}

class BucketSettings(val pos: (Int, Int),
                     val size: (Int, Int),
                     val scene: Scene,
                     val samplesRoot: Int,
                     val samplerStrategy: SamplerStrategy) {
  val samplesPerPixel = samplesRoot * samplesRoot
}