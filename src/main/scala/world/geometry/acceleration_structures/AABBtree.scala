package com.beardedplatypus.world.geometry.acceleration_structures

import com.beardedplatypus.math.{Cons, Point3d, Ray}
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.world.geometry.{FiniteGeometricObject, GeometricObject}
import world.geometry.acceleration_structures.DepthTracing

import scala.annotation.tailrec
import scala.util._

class AABBTree(val root: AABBNode) extends AccelerationStructure {
  def intersectDistance(ray: Ray): Option[Double] = {
    if (root.intersectDistanceToBox(ray).nonEmpty) root.intersectDistance(ray)
    else None
  }

  def intersect(ray: Ray): Option[RayResult] = {
    if (root.intersectDistanceToBox(ray).nonEmpty) root.intersect(ray)
    else None
  }
  def isVisible(ray: Ray, distance: Double): Boolean = {
    val tMin = root.intersectDistanceToBox(ray)
    if (tMin.nonEmpty && tMin.get < distance) root.isVisible(ray, distance)
    else true
  }
}

object AABBTree {
  def construct(geometryObjects: List[BoundingBoxable]): AABBTree = {

    val t0: Double = System.nanoTime()
    val boundingBoxes: Array[AABBNode] = geometryObjects.toArray map (_.boundingBox)
    val time1: Double = System.nanoTime()
    println("Converting bb to array: " + ((time1 - t0) / 1000000000.0))

    //    def constructRecursive(ls: List[AABBNode], countDepth: Int): AABBNode = ls match {
//      case x :: Nil => x
//      case head :: lTail => {
//        var x0: Double = head.bounds._1.x
//        var x1: Double = head.bounds._2.x
//
//        var y0: Double = head.bounds._1.y
//        var y1: Double = head.bounds._2.y
//
//        var z0: Double = head.bounds._1.z
//        var z1: Double = head.bounds._2.z
//
//        for (node <- lTail) {
//          if (node.bounds._1.x < x0) x0 = node.bounds._1.x
//          if (node.bounds._2.x > x1) x1 = node.bounds._2.x
//
//          if (node.bounds._1.y < y0) y0 = node.bounds._1.y
//          if (node.bounds._2.y > y1) y1 = node.bounds._2.y
//
//          if (node.bounds._1.z < z0) z0 = node.bounds._1.z
//          if (node.bounds._2.z > z1) z1 = node.bounds._2.z
//        }
//
//        val xLength = x1 - x0
//        val yLength = y1 - y0
//        val zLength = z1 - z0
//
//        if (x0 > x1) println("x: " + x0 + " | " + x1)
//        if (y0 > y1) println("y: " + y0 + " | " + y1)
//        if (z0 > z1) println("z: " + z0 + " | " + z1)
//
//
//        val sortedLs = {
//          if (xLength > yLength && xLength > zLength) ls.sortBy(_.bounds._1.x)
//          else if (yLength > zLength) ls.sortBy(_.bounds._1.y)
//          else ls.sortBy(_.bounds._1.z)
//        }
//
//        val nodes = sortedLs.splitAt(sortedLs.length / 2)
//
//        AABBNode.branch(new Point3d(x0, y0, z0), new Point3d(x1, y1, z1),
//                        constructRecursive(nodes._1, countDepth + 1),
//                        constructRecursive(nodes._2, countDepth + 1))
//      }
//      case x => {
//        println(x)
//        throw new IllegalArgumentException()
//      }
//    }

    def constructRecursive(ls: Array[AABBNode], countDepth: Int): AABBNode = {
      if (ls.length == 1) ls.head
      else {
        var node = ls.head
        var x0: Double = node.bounds._1.x
        var x1: Double = node.bounds._2.x

        var y0: Double = node.bounds._1.y
        var y1: Double = node.bounds._2.y

        var z0: Double = node.bounds._1.z
        var z1: Double = node.bounds._2.z

        for (iNode <- 1 to ls.length -1) {
          node = ls(iNode)
          if (node.bounds._1.x < x0) x0 = node.bounds._1.x
          if (node.bounds._2.x > x1) x1 = node.bounds._2.x

          if (node.bounds._1.y < y0) y0 = node.bounds._1.y
          if (node.bounds._2.y > y1) y1 = node.bounds._2.y

          if (node.bounds._1.z < z0) z0 = node.bounds._1.z
          if (node.bounds._2.z > z1) z1 = node.bounds._2.z
        }

        val xLength = x1 - x0
        val yLength = y1 - y0
        val zLength = z1 - z0

        val sortedLs = {
          if (xLength > yLength && xLength > zLength) ls.sortBy(_.bounds._1.x)
          else if (yLength > zLength) ls.sortBy(_.bounds._1.y)
          else ls.sortBy(_.bounds._1.z)
        }


        val nodes = sortedLs.splitAt(sortedLs.length / 2)

        AABBNode.branch(new Point3d(x0, y0, z0), new Point3d(x1, y1, z1),
          constructRecursive(nodes._1, countDepth + 1),
          constructRecursive(nodes._2, countDepth + 1))
      }
    }
    new AABBTree(constructRecursive(boundingBoxes, 0))
  }
}

// ----------------------------------------------------------------------------
abstract class AABBNode(val bounds: (Point3d, Point3d)) {
  def intersectDistance(ray: Ray): Option[Double]
  def intersect(ray: Ray): Option[RayResult]
  def isVisible(ray: Ray, distance: Double): Boolean

//  protected def isIntersectBy(ray: Ray): Boolean = {
//    // Currently not interested in computing the actual distance.
//    var tmin: Double = ((if (ray.sign._1) bounds._2.x else bounds._1.x) - ray.origin.x) * ray.invDirection.x
//    var tmax: Double = ((if (ray.sign._1) bounds._1.x else bounds._2.x) - ray.origin.x) * ray.invDirection.x
//
//    val tymin: Double = ((if (ray.sign._2) bounds._2.y else bounds._1.y) - ray.origin.y) * ray.invDirection.y
//    val tymax: Double = ((if (ray.sign._2) bounds._1.y else bounds._2.y) - ray.origin.y) * ray.invDirection.y
//
//    if ((tmin > tymax) || (tymin > tmax)) false
//    else {
//      if (tymin > tmin) tmin = tymin
//      if (tymax < tmax) tmax = tymax
//
//      val tzmin: Double = ((if (ray.sign._3) bounds._2.z else bounds._1.z) - ray.origin.z) * ray.invDirection.z
//      val tzmax: Double = ((if (ray.sign._3) bounds._1.z else bounds._2.z) - ray.origin.z) * ray.invDirection.z
//
//      if ((tmin > tzmax) || (tzmin > tmax)) false
//      else true
//    }
//  }

  def intersectDistanceToBox(ray: Ray): Option[Double] = {
    // Currently not interested in computing the actual distance.
    var tmin: Double = ((if (ray.sign._1) bounds._2.x else bounds._1.x) - ray.origin.x) * ray.invDirection.x
    var tmax: Double = ((if (ray.sign._1) bounds._1.x else bounds._2.x) - ray.origin.x) * ray.invDirection.x

    val tymin: Double = ((if (ray.sign._2) bounds._2.y else bounds._1.y) - ray.origin.y) * ray.invDirection.y
    val tymax: Double = ((if (ray.sign._2) bounds._1.y else bounds._2.y) - ray.origin.y) * ray.invDirection.y

    if ((tmin > tymax) || (tymin > tmax)) None
    else {
      if (tymin > tmin) tmin = tymin
      if (tymax < tmax) tmax = tymax

      val tzmin: Double = ((if (ray.sign._3) bounds._2.z else bounds._1.z) - ray.origin.z) * ray.invDirection.z
      val tzmax: Double = ((if (ray.sign._3) bounds._1.z else bounds._2.z) - ray.origin.z) * ray.invDirection.z

      if ((tmin > tzmax) || (tzmin > tmax)) None
      else {
        Option(if (tzmin > tmin) tzmin else tmin)
      }
    }
  }

  override def toString: String = "AABBNode: Bounds: " + bounds
}

object AABBNode {
  def leaf(minBound: Point3d, maxBound: Point3d,
           obj: GeometricObject): AABBNode = {
    new AABBLeafNode((minBound, maxBound), obj) //with DepthTracing
  }

  def branch(minBound: Point3d, maxBound: Point3d,
             leftChild: AABBNode, rightChild: AABBNode): AABBNode = {
    new AABBBranchNode((minBound, maxBound), leftChild, rightChild) //with DepthTracing
  }
}

// ----------------------------------------------------------------------------
class AABBLeafNode(tBounds: (Point3d, Point3d),
                   val obj: GeometricObject) extends AABBNode(tBounds) {

  override def intersectDistance(ray: Ray): Option[Double] = {
    obj.intersectDistance(ray)
  }

  override def intersect(ray: Ray): Option[RayResult] = {
    obj.intersect(ray)
  }

  override def isVisible(ray: Ray, distance: Double): Boolean = {
    if ((this intersectDistanceToBox  ray).isEmpty) true
    else {
      val tOption = obj.intersectDistance(ray)
      tOption.isEmpty || tOption.get > distance || tOption.get < Cons.kEpsilon
    }
  }
}

// ----------------------------------------------------------------------------
// FIXME naming of order
class AABBBranchNode(tBounds: (Point3d, Point3d),
                     val leftChild: AABBNode, val rightChild: AABBNode) extends AABBNode(tBounds) {
  override def intersectDistance(ray: Ray): Option[Double] = {
    val tLeft: Option[Double] = leftChild.intersectDistanceToBox(ray)
    val tRight: Option[Double] = rightChild.intersectDistanceToBox(ray)

    (tLeft.nonEmpty, tRight.nonEmpty) match {
      case (false, false) => None
      case (true, false) => leftChild.intersectDistance(ray)
      case (false, true) => rightChild.intersectDistance(ray)
      case (true, true) => {
        val order = if (tLeft.get < tRight.get) (leftChild, (rightChild, tRight))
                    else (rightChild, (leftChild, tLeft))

        val closestBox: Option[Double] = order._1.intersectDistance(ray)
        if (closestBox.isEmpty) order._2._1.intersectDistance(ray)
        else if (closestBox.get < order._2._2.get) closestBox
        else {
          val otherBox = order._2._1.intersectDistance(ray)
          if (otherBox.isEmpty) closestBox
          else Option(Math.min(closestBox.get, otherBox.get))
        }
      }
    }
  }

  override def intersect(ray: Ray): Option[RayResult] = {
    val tLeft: Option[Double] = leftChild.intersectDistanceToBox(ray)
    val tRight: Option[Double] = rightChild.intersectDistanceToBox(ray)

    (tLeft.nonEmpty, tRight.nonEmpty) match {
      case (false, false) => None
      case (true, false) => leftChild.intersect(ray)
      case (false, true) => rightChild.intersect(ray)
      case (true, true) => {
        val order = if (tLeft.get < tRight.get) (leftChild, (rightChild, tRight))
                    else (rightChild, (leftChild, tLeft))

        val closestBox: Option[RayResult] = order._1.intersect(ray)
        if (closestBox.isEmpty) order._2._1.intersect(ray)
        else if (closestBox.get.distance < order._2._2.get) closestBox
        else {
          val otherBox = order._2._1.intersect(ray)
          if (otherBox.isEmpty) closestBox
          else if (closestBox.get.distance < otherBox.get.distance) closestBox else otherBox
        }
      }
    }


//    if (this isIntersectBy ray) {
//      val resultLeft: Option[RayResult] = leftChild.intersect(ray)
//      val resultRight: Option[RayResult] = rightChild.intersect(ray)
//
//      (resultLeft.nonEmpty, resultRight.nonEmpty) match {
//        case (false, false) => None
//        case (true, false) => resultLeft
//        case (false, true) => resultRight
//        case (true, true) => if (resultLeft.get.distance < resultRight.get.distance) resultLeft else resultRight
//      }
//    } else None
  }

  // FIXME: add distance check on nodes already (if t0 exceeds distance, it doesn't need to be checked regardless whether it hits the box)
  override def isVisible(ray: Ray, distance: Double): Boolean = {
    val tLeft: Option[Double] = leftChild.intersectDistanceToBox(ray)
    val tRight: Option[Double] = rightChild.intersectDistanceToBox(ray)

    (tLeft.nonEmpty && tLeft.get < distance, tRight.nonEmpty && tRight.get < distance) match {
      case (false, false) => true
      case (true, false) => leftChild.isVisible(ray, distance)
      case (false, true) => rightChild.isVisible(ray, distance)
      case (true, true) =>  leftChild.isVisible(ray, distance) && rightChild.isVisible(ray, distance)
    }
  }
}