package com.beardedplatypus.world.geometry.objects.polygon

import com.beardedplatypus.math.{Point3d, Vector3d}

class Vertex(val normal: Vector3d,
             val origin: Point3d) {
  override def toString: String = "Vertex: origin: " + origin +" | normal: " + normal
}

object Vertex {
  def apply(normal: Vector3d, origin: Point3d): Vertex = new Vertex(normal, origin)
}