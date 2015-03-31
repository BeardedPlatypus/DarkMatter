package com.beardedplatypus.math

class OrthonormalBasis(val u: Vector3d,
                       val v: Vector3d,
                       val w: Vector3d) {
}

object OrthonormalBasis {
  // Assumption, DenseVectors are already normalized
  def apply(u: Vector3d,
            v: Vector3d,
            w: Vector3d): OrthonormalBasis = new OrthonormalBasis(u, v, w)

  def apply(a: Vector3d): OrthonormalBasis = {
    val w = a.normalized
    val b = {if (w.x > w.y) Vector3d(0.0, 1.0, 0.0)
             else Vector3d(1.0, 0.0, 0.0) }
    val u = (b cross w).normalized
    val v =  w cross u

    OrthonormalBasis(u, v, w)
  }

  def apply(a: Vector3d, b: Vector3d): OrthonormalBasis = {
    val w = a.normalized
    val u = (b cross w).normalized
    val v = w cross u

    OrthonormalBasis(u, v, w)
  }
}
