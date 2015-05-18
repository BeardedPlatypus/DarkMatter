package com.beardedplatypus.camera

import com.beardedplatypus.math.{Vector3d, Ray, Point3d, OrthonormalBasis}
import com.beardedplatypus.sampling.Sample
import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy

// combined camera / viewplane
// TODO add gamma
// TODO replace width height by pixelsize
abstract class Camera(val xRes: Int, val yRes: Int, val origin: Point3d) {
  def generatePrimaryRay(sample: Sample, branchingFactor: Int, samplerStrategy: SamplerStrategy): Ray
}

class PerspectiveCamera(xRes: Int, yRes: Int, origin: Point3d,
                        val basis: OrthonormalBasis,
                        val width: Double,
                        val height: Double) extends Camera(xRes, yRes, origin) {

  def generatePrimaryRay(sample: Sample, branchingFactor: Int, samplerStrategy: SamplerStrategy): Ray = {
    val u: Double = this.width * ((sample.x / xRes.toDouble) - 0.5)
    val v: Double = this.height * ((sample.y / yRes.toDouble) - 0.5)

    val dir: Vector3d = this.basis.w + this.basis.u * u + this.basis.v * v
    Ray(this.origin, dir, sample, 0, branchingFactor, samplerStrategy)
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