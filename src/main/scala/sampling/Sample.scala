package com.beardedplatypus.sampling

class Sample(val x: Double, val y: Double,
             val u: Int, val v: Int) {
  override def toString = "Sample | x: " + x + " y: " + y + "\n       | u: " + u + " v: " + v
}

object Sample {
  def apply(u: Int, v: Int): Sample = new Sample(u.toDouble + 0.5, v.toDouble + 0.5, u, v)
  //def apply(x: Double, y: Double): Sample = new Sample(x, y)
}