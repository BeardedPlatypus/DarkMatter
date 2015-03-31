package com.beardedplatypus.math

// Test implementation, basic calculations instead of DenseMatrices
class HomogeneousMatrix(val a11: Double, val a12: Double, val a13: Double, val a14: Double,
                        val a21: Double, val a22: Double, val a23: Double, val a24: Double,
                        val a31: Double, val a32: Double, val a33: Double, val a34: Double,
                        val a41: Double, val a42: Double, val a43: Double, val a44: Double) {

  def +(that: HomogeneousMatrix): HomogeneousMatrix = {
    new HomogeneousMatrix(
      this.a11 + that.a11, this.a12 + that.a12, this.a13 + that.a13, this.a14 + that.a14,
      this.a21 + that.a21, this.a22 + that.a22, this.a23 + that.a23, this.a24 + that.a24,
      this.a31 + that.a31, this.a32 + that.a32, this.a33 + that.a33, this.a34 + that.a34,
      this.a41 + that.a41, this.a42 + that.a42, this.a43 + that.a43, this.a44 + that.a44
    )
  }

  def -(that: HomogeneousMatrix): HomogeneousMatrix = {
    new HomogeneousMatrix(
      this.a11 - that.a11, this.a12 - that.a12, this.a13 - that.a13, this.a14 - that.a14,
      this.a21 - that.a21, this.a22 - that.a22, this.a23 - that.a23, this.a24 - that.a24,
      this.a31 - that.a31, this.a32 - that.a32, this.a33 - that.a33, this.a34 - that.a34,
      this.a41 - that.a41, this.a42 - that.a42, this.a43 - that.a43, this.a44 - that.a44
    )
  }

  def *(that: Double): HomogeneousMatrix = {
    new HomogeneousMatrix(
      this.a11 * that, this.a12 * that, this.a13 * that, this.a14 * that,
      this.a21 * that, this.a22 * that, this.a23 * that, this.a24 * that,
      this.a31 * that, this.a32 * that, this.a33 * that, this.a34 * that,
      this.a41 * that, this.a42 * that, this.a43 * that, this.a44 * that
    )
  }

  def *(that: HomogeneousMatrix): HomogeneousMatrix = {
    new HomogeneousMatrix(
      this.a11 * that.a11 + this.a12 * that.a21 + this.a13 * that.a31 + this.a14 * that.a41,
      this.a11 * that.a12 + this.a12 * that.a22 + this.a13 * that.a32 + this.a14 * that.a42,
      this.a11 * that.a13 + this.a12 * that.a23 + this.a13 * that.a33 + this.a14 * that.a43,
      this.a11 * that.a14 + this.a12 * that.a24 + this.a13 * that.a34 + this.a14 * that.a44,

      this.a21 * that.a11 + this.a22 * that.a21 + this.a23 * that.a31 + this.a24 * that.a41,
      this.a21 * that.a12 + this.a22 * that.a22 + this.a23 * that.a32 + this.a24 * that.a42,
      this.a21 * that.a13 + this.a22 * that.a23 + this.a23 * that.a33 + this.a24 * that.a43,
      this.a21 * that.a14 + this.a22 * that.a24 + this.a23 * that.a34 + this.a24 * that.a44,

      this.a31 * that.a11 + this.a32 * that.a21 + this.a33 * that.a31 + this.a34 * that.a41,
      this.a31 * that.a12 + this.a32 * that.a22 + this.a33 * that.a32 + this.a34 * that.a42,
      this.a31 * that.a13 + this.a32 * that.a23 + this.a33 * that.a33 + this.a34 * that.a43,
      this.a31 * that.a14 + this.a32 * that.a24 + this.a33 * that.a34 + this.a34 * that.a44,

      this.a41 * that.a11 + this.a42 * that.a21 + this.a43 * that.a31 + this.a44 * that.a41,
      this.a41 * that.a12 + this.a42 * that.a22 + this.a43 * that.a32 + this.a44 * that.a42,
      this.a41 * that.a13 + this.a42 * that.a23 + this.a43 * that.a33 + this.a44 * that.a43,
      this.a41 * that.a14 + this.a42 * that.a24 + this.a43 * that.a34 + this.a44 * that.a44
    )
  }

  def *(that: Vector3d): Vector3d = {
    new Vector3d(
      this.a11 * that.x + this.a12 * that.y + this.a13 * that.z,
      this.a21 * that.x + this.a22 * that.y + this.a23 * that.z,
      this.a31 * that.x + this.a32 * that.y + this.a33 * that.z
    )
  }

  def *(that: Point3d): Point3d = {
    new Point3d(
      this.a11 * that.x + this.a12 * that.y + this.a13 * that.z + this.a14,
      this.a21 * that.x + this.a22 * that.y + this.a23 * that.z + this.a24,
      this.a31 * that.x + this.a32 * that.y + this.a33 * that.z + this.a34
    )
  }

  lazy val t: HomogeneousMatrix = {
    new HomogeneousMatrix(
      this.a11, this.a21, this.a31, this.a41,
      this.a12, this.a22, this.a32, this.a42,
      this.a13, this.a23, this.a33, this.a43,
      this.a14, this.a24, this.a34, this.a44
    )
  }

  override def toString: String = f"Matrix: \n$a11%2.2f $a12%2.2f $a13%2.2f $a14%2.2f\n$a21%2.2f $a22%2.2f $a23%2.2f $a24%2.2f\n $a31%2.2f $a32%2.2f $a33%2.2f $a34%2.2f\n$a41%2.2f $a42%2.2f $a43%2.2f $a44%2.2f\n"
}

object HomogeneousMatrix {
  val eye: HomogeneousMatrix = new HomogeneousMatrix(1, 0, 0, 0,
                                                     0, 1, 0, 0,
                                                     0, 0, 1, 0,
                                                     0, 0, 0, 1)
}
