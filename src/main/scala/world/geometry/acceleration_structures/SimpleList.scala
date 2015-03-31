package com.beardedplatypus.world.geometry.acceleration_structures

import com.beardedplatypus.math.{Cons, Ray}
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.world.geometry.GeometricObject

class SimpleList(val geometricObjects: List[GeometricObject]) extends AccelerationStructure {
  def intersectDistance(ray: Ray): Option[Double] = {
    val res = (geometricObjects.view map((x: GeometricObject) => (x.intersectDistance(ray)))).par.flatten
    if (res.isEmpty) None else Option(res.min)
  }

  def intersect(ray: Ray): Option[RayResult] = {
    val res = (geometricObjects.view map((x: GeometricObject) => (x.intersect(ray)))).par.flatten
    (if (res.isEmpty) None else Option(res minBy (_.distance)))
  }

  def isVisible(ray: Ray, distance: Double): Boolean = {
    var list: List[GeometricObject] = geometricObjects
    while (!list.isEmpty) {
      val tOption: Option[Double] = list.head.intersectDistance(ray)
      if (list.head.castsShadow && (tOption.nonEmpty && (tOption.get < distance) && (tOption.get > Cons.kEpsilon))) return false
      list = list.tail
    }
    true
  }

}
