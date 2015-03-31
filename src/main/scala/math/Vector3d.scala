package com.beardedplatypus.math

class Vector3d(val x: Double, val y: Double, val z: Double) {
  def +(that: Vector3d): Vector3d = new Vector3d(this.x + that.x, this.y + that.y, this.z + that.z)
  def -(that: Vector3d): Vector3d = new Vector3d(this.x - that.x, this.y - that.y, this.z - that.z)
  def *(that: Double): Vector3d = new Vector3d(this.x * that, this.y * that, this.z * that)
  def /(that: Double): Vector3d = new Vector3d(this.x / that, this.y / that, this.z / that)


  def inverted: Vector3d = this * -1
  def normalized: Vector3d = this / Math.sqrt(x * x + y * y + z * z)

  def dot(that: Vector3d): Double = this.x * that.x + this.y * that.y + this.z * that.z
  def dot(that: Point3d): Double = this.x * that.x + this.y * that.y + this.z * that.z

  def cross(that: Vector3d): Vector3d = Vector3d(this.y * that.z - this.z * that.y,
                                                 this.z * that.x - this.x * that.z,
                                                 this.x * that.y - this.y * that.x)

  def normSq: Double = this.x * this.x + this.y * this.y + this.z * this.z
  override def toString: String = f"Vector3d: x: $x%2.2f, y: $y%2.2f, z: $z%2.2f"
}

object Vector3d {
  def apply(x: Double, y: Double, z: Double): Vector3d = new Vector3d(x, y, z)
  val zero = Vector3d(0.0, 0.0, 0.0)

}