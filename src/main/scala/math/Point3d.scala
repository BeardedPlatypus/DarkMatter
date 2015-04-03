package com.beardedplatypus.math

class Point3d(val x: Double, val y: Double, val z: Double) {
  def +(that: Vector3d): Point3d = new Point3d(this.x + that.x, this.y + that.y, this.z + that.z)
  def -(that: Point3d): Vector3d = new Vector3d(this.x - that.x, this.y - that.y, this.z - that.z)
  def *(that: Double): Point3d = new Point3d(this.x * that, this.y * that, this.z * that)
  def /(that: Double): Point3d = new Point3d(this.x / that, this.y / that, this.z / that)

  def dot(that: Vector3d): Double = this.x * that.x + this.y * that.y + this.z * that.z
  def dot(that: Point3d): Double = this.x * that.x + this.y * that.y + this.z * that.z

  def toVector3d = new Vector3d(x, y, z)

  override def toString: String = f"Point3d: x: $x%2.2f, y: $y%2.2f, z: $z%2.2f"
}

object Point3d {
  val origin = new Point3d(0.0, 0.0, 0.0)
  val NaN = new Point3d(Double.NaN, Double.NaN, Double.NaN)
}