package com.beardedplatypus.shading

class ColorRGB(val r: Int, val g: Int, val b: Int, val a: Int) {
  require(r >= 0 && r < 256)
  require(g >= 0 && g < 256)
  require(b >= 0 && b < 256)
  require(a >= 0 && a < 256)
}

object ColorRGB {
  def apply(r: Int, g: Int, b: Int, a: Int): ColorRGB = new ColorRGB(clamp(r),
                                                                     clamp(g),
                                                                     clamp(b),
                                                                     clamp(a))
  def apply(r: Int, g: Int, b: Int): ColorRGB = ColorRGB(r, g, b, 255)

  def clamp(x: Int): Int = (x max 0) min 255
}

class Color(val r: Double, val g: Double, val b: Double, val a: Double) {
  def *(x: Double): Color = Color(r * x, g * x, b * x)
  def /(x: Double): Color = Color(r / x, g / x, b / x)

  def +(that: Color): Color = Color(this.r + that.r,
                                    this.g + that.g,
                                    this.b + that.b)
  def *(that: Color): Color = Color(this.r * that.r,
                                    this.g * that.g,
                                    this.b * that.b)

  def toRGB: ColorRGB = ColorRGB((r * 255).toInt,
                                 (g * 255).toInt,
                                 (b * 255).toInt,
                                 (a * 255).toInt)

  def clamp: Color = Color(Color.clamp(r), Color.clamp(g), Color.clamp(b), Color.clamp(a))

  override def toString = f"Color: r: $r%.2f, g: $g%.2f, b: $b%.2f"
}

object Color {
  def apply(r: Double, g: Double, b: Double, a: Double): Color = new Color(r, g, b, a)
  def apply(r: Double, g: Double, b: Double): Color = Color(r, g, b, 1.0)

  val black = Color(0, 0, 0, 1.0)
  val white = Color(1.0, 1.0, 1.0, 1.0)
  val red = Color(1.0, 0.0, 0.0)
  val green = Color(0.0, 1.0, 0.0)
  val blue = Color(0.0, 0.0, 1.0)
  val gray = Color(0.5, 0.5, 0.5)

  val orange = Color(1.0, 0.65, 0)
  val gold = Color(1.0, 0.84, 0)
  val magenta = Color(1, 0, 1)
  val yellow = Color(1, 1, 0)

  def clamp(x: Double): Double = (x max 0.0) min 1.0
}