package com.beardedplatypus.camera

import com.beardedplatypus.math.{Vector3d, Ray, Point3d, OrthonormalBasis}
import com.beardedplatypus.sampling.Sample

// combined camera / viewplane
// TODO add gamma
// TODO replace width height by pixelsize
abstract class Camera(val xRes: Int, val yRes: Int, val origin: Point3d) {
  def generateRay(sample: Sample): Ray
}

class PerspectiveCamera(xRes: Int, yRes: Int, origin: Point3d,
                        val basis: OrthonormalBasis,
                        val width: Double,
                        val height: Double) extends Camera(xRes, yRes, origin) {

  def generateRay(sample: Sample): Ray = {
    val u: Double = this.width * ((sample.x / xRes.toDouble) - 0.5)
    val v: Double = this.height * ((sample.y / yRes.toDouble) - 0.5)

    // TODO: figure out why I need to cast these values first, before adding
    val uComp: Vector3d = this.basis.u * u
    val vComp: Vector3d = this.basis.v * v

    val dir: Vector3d = this.basis.w + uComp + vComp
    Ray(this.origin, dir, sample)
  }
}

object PerspectiveCamera {
  def apply(xRes: Int, yRes: Int,
            o: Point3d, b: OrthonormalBasis,
            width: Double, height: Double): PerspectiveCamera = new PerspectiveCamera(xRes, yRes, o, b, width, height)

  def apply(xRes: Int, yRes: Int,
            origin: Point3d,
            lookAt: Vector3d, up: Vector3d,
            fov: Double): PerspectiveCamera = {
    require(xRes > 1)
    require(yRes > 1)
    require(fov < 180)

    val b: OrthonormalBasis = OrthonormalBasis(lookAt, up)
    val width = 2.0 * Math.tan(0.5 * Math.toRadians(fov))
    val height = yRes.toDouble * width / xRes.toDouble

    PerspectiveCamera(xRes, yRes, origin, b, width, height)
  }
}