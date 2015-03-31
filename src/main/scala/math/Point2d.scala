package com.beardedplatypus.math

class Point2d(val u: Double, val v: Double) {
  def +(that: Point2d): Point2d = new Point2d(this.u + that.u, this.v + that.v)
  def *(that: Double): Point2d = new Point2d(this.u * that, this.v * that)
}

object Point2d {
  val invalid = invalidPoint2d
}

object invalidPoint2d extends Point2d(Double.NaN, Double.NaN) {
  override def +(that: Point2d): Point2d = this
  override def *(that: Double): Point2d = this
}
