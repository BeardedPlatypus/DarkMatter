package com.beardedplatypus.shading

abstract class ColorMap {
  def toColor(x: Double): Color
}

// FIXME: fix hardcoded values.
class CubeHelix(val start: Double,
                val rotations: Double,
                val hue: Double,
                val reverse: Boolean) extends ColorMap {

  def toColor(lambda: Double) = {
    val phi: Double = 2.0 * Math.PI * (start / 3.0 + rotations * lambda)
    val a: Double = hue * lambda * (1 - lambda) * 0.5

    val cosPhi: Double = Math.cos(phi)
    val sinPhi: Double = Math.sin(phi)

    val red: Double = lambda + a * (-0.14861 * cosPhi + 1.78277 * sinPhi)
    val green: Double = lambda + a * (-0.29227 * cosPhi - 0.90649 * sinPhi)
    val blue: Double = lambda + a * 1.97294 * cosPhi
    Color(red, green, blue)
  }
}
