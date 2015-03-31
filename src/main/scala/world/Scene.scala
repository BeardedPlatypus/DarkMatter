package com.beardedplatypus.world

import com.beardedplatypus.camera.Camera
import com.beardedplatypus.math.{Cons, Ray}
import com.beardedplatypus.sampling.Sample
import com.beardedplatypus.shading.{RayResult, Color}
import com.beardedplatypus.world.geometry.GeometricObject
import com.beardedplatypus.world.geometry.acceleration_structures.{AccelerationStructure, AABBNode}

class Scene(val camera: Camera,
            val geometricObjects: AccelerationStructure,
            val ambientLight: Light,
            val lights: List[Light],
            val backgroundColor: Color) {

  def intersectGeometry(ray: Ray): IntersectResult = new IntersectResult(ray.sample, geometricObjects.intersect(ray))
  // FIXME visible function self intersection
  def visible(ray: Ray, h: Double): Boolean = geometricObjects.isVisible(ray, h)
}


class IntersectResult(val sample: Sample, val hitResult: Option[RayResult])