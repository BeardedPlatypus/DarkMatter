package world.geometry.acceleration_structures

import com.beardedplatypus.math.Ray
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.world.geometry.acceleration_structures.AABBNode

trait DepthTracing extends AABBNode {
//  abstract override def intersectDistance(ray: Ray): Option[Double] = {
//    DepthTraceBuffer.main.increment(ray.sample.u, ray.sample.v)
//    super.intersectDistance(ray)
//  }
//  abstract override def intersect(ray: Ray): Option[RayResult] = {
//    DepthTraceBuffer.main.increment(ray.sample.u, ray.sample.v)
//    super.intersect(ray)
//  }
  abstract override def intersectDistanceToBox(ray: Ray): Option[Double] = {
    DepthTraceBuffer.main.increment(ray.sample.u, ray.sample.v)
    super.intersectDistanceToBox(ray)
  }
}


object DepthTraceBuffer {
  var main: DepthTraceBuffer = null
}

class DepthTraceBuffer(val width: Int, val height: Int) {
  private val depthTraceBuffer: Array[Array[Int]] = Array.ofDim(height, width)

  def increment(x: Int, y: Int): Unit = {
    depthTraceBuffer(y)(x) += 1
  }

  def getBuffer: Array[Array[Int]] = this.depthTraceBuffer.clone()
  def getNormalisedBuffer: Array[Array[Double]] = {
    val max: Double = depthTraceBuffer.flatten.max
    println(max)
    val depthTraceBufferC = depthTraceBuffer.clone()
    depthTraceBufferC map ((x: Array[Int]) => x map ((v: Int) => v.toDouble / max))
  }
}