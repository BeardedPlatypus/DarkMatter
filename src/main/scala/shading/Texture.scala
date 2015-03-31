package com.beardedplatypus.shading

import java.io.File
import javax.imageio.ImageIO

import com.beardedplatypus.math.Point2d

abstract class Texture {
  def colorAt(p: Point2d): Color
}

object Texture {
  def constant(c: Color): Texture = new ConstantColorTexture(c)
  def image(filepath: String): Texture = new ImageTexture(filepath)
}

private class ConstantColorTexture(val c: Color) extends Texture {
  override def colorAt(p: Point2d): Color = c
}

private class ImageTexture(filepath: String) extends Texture {
//  val imageColor: Vector[Vector[Color]] = {
//    val div = 1 / 255
//    val image = ImageIO.read(new File(filepath))
//    (for (y <- 0 to image.getHeight - 1) yield {
//      (for (x <- 0 to image.getWidth - 1) yield {
//        val pixel = image.getRGB(x, y)
//        val alpha = (pixel >> 24) & 0xff;
//        val red = (pixel >> 16) & 0xff;
//        val green = (pixel >> 8) & 0xff;
//        val blue = (pixel) & 0xff;
//
//        new Color(red * div, green * div, blue * div, alpha * div)
//      }).toVector
//    }).toVector
//  }

  val imageColor = ImageIO.read(new File(filepath))

  val height = imageColor.getWidth
  val width = imageColor.getHeight

  override def colorAt(p: Point2d): Color = {
    val uIndex = (p.u * width).toInt
    val vIndex = ((1 - p.v) * height).toInt

    val pixel = imageColor.getRGB(uIndex, vIndex)
    val alpha = (pixel >> 24) & 0xff
    val red = (pixel >> 16) & 0xff
    val green = (pixel >> 8) & 0xff
    val blue = (pixel) & 0xff

    val div = 1.0 / 255.0
    new Color(red * div, green * div, blue * div, alpha * div)
  }
}