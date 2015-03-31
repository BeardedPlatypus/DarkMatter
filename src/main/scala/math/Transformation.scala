package com.beardedplatypus.math

class Transformation(val matrix: HomogeneousMatrix, val invMatrix: HomogeneousMatrix) {
  def invert = new Transformation(invMatrix, matrix)

  def transform(p: Point3d): Point3d = this.matrix * p
  def transformInv(p: Point3d): Point3d = this.invMatrix * p

  def transform(v: Vector3d): Vector3d = this.matrix * v
  def transformInv(v: Vector3d): Vector3d = this.invMatrix * v
  def transformInvTranspose(v: Vector3d): Vector3d = this.invMatrix.t * v // TODO evaluate whether it matters to cache this or not.

  def transform(r: Ray): Ray = Ray(this.transform(r.origin), this.transform(r.direction), r.sample)
  def transformInv(r: Ray): Ray = Ray(this.transformInv(r.origin), this.transformInv(r.direction), r.sample)

  def *(that: Transformation): Transformation = new Transformation(this.matrix * that.matrix, that.invMatrix * this.invMatrix)
}

object Transformation {
  val identity = new Transformation(HomogeneousMatrix.eye, HomogeneousMatrix.eye)

  def translation(x: Double, y: Double, z: Double): Transformation = {
    new Transformation(
      new HomogeneousMatrix(
        1.0, 0.0, 0.0,   x,
        0.0, 1.0, 0.0,   y,
        0.0, 0.0, 1.0,   z,
        0.0, 0.0, 0.0, 1.0
      ),
      new HomogeneousMatrix(
        1.0, 0.0, 0.0, -x,
        0.0, 1.0, 0.0, -y,
        0.0, 0.0, 1.0, -z,
        0.0, 0.0, 0.0, 1.0
      ))
  }

  def rotate(axis: String, angle: Double): Transformation = {
    val rad = Math.toRadians(angle)
    val sin = Math.sin(rad)
    val cos = Math.cos(rad)

    val matrix = {
      axis match {
        case "x" | "X" => {
          new HomogeneousMatrix(
            1.0, 0.0,  0.0, 0.0,
            0.0, cos, -sin, 0.0,
            0.0, sin,  cos, 0.0,
            0.0, 0.0,  0.0, 1.0
          )
        }
        case "y" | "Y" => {
          new HomogeneousMatrix(
             cos, 0.0, sin, 0.0,
             0.0, 1.0, 0.0, 0.0,
            -sin, 0.0, cos, 0.0,
             0.0, 0.0, 0.0, 1.0
          )
        }
        case "z" | "Z" => {
          new HomogeneousMatrix(
            cos, -sin, 0.0, 0.0,
            sin,  cos, 0.0, 0.0,
            0.0,  0.0, 1.0, 0.0,
            0.0,  0.0, 0.0, 1.0
          )
        }
        case _ => throw new IndexOutOfBoundsException()
      }
    }
    new Transformation(matrix, matrix.t)
  }

  def scale(x: Double, y: Double, z: Double): Transformation = {
    new Transformation(new HomogeneousMatrix(x, 0, 0, 0,
                                             0, y, 0, 0,
                                             0, 0, z, 0,
                                             0, 0, 0, 1),
                       new HomogeneousMatrix(1.0 / x,   0.0,    0.0,   0.0,
                                                0.0, 1.0 / y,   0.0,   0.0,
                                                0.0,    0.0, 1.0 / z,  0.0,
                                                0.0,    0.0,    0.0,   1.0)
    )
  }
}