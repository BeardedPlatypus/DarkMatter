package com.beardedplatypus.world.geometry.objects.polygon

import com.beardedplatypus.math._
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.geometry.{FiniteGeometricObject, GeometricObject}
import com.beardedplatypus.world.geometry.acceleration_structures.AABBNode

object MeshTriangle {
  def apply(v1: Vertex, v2: Vertex, v3: Vertex,
            uv1: Point2d, uv2: Point2d, uv3: Point2d,
            normal: Vector3d,transformation: Transformation,
            mat: Material,  shadow: Boolean, isFlat: Boolean): MeshTriangle = {
    if (isFlat) new FlatMeshTriangle(v1, v2, v3, uv1, uv2, uv3, normal, transformation, mat, shadow)
    else new SmoothMeshTriangle(v1, v2, v3, uv1, uv2, uv3, normal, transformation, mat, shadow)
  }

  def flat(v1: Vertex, v2: Vertex, v3: Vertex,
           uv1: Point2d, uv2: Point2d, uv3: Point2d,
           normal: Vector3d,transformation: Transformation,
           mat: Material,  shadow: Boolean): MeshTriangle = new FlatMeshTriangle(v1, v2, v3, uv1, uv2, uv3, normal, transformation, mat, shadow)
  def smooth(v1: Vertex, v2: Vertex, v3: Vertex,
             uv1: Point2d, uv2: Point2d, uv3: Point2d,
             normal: Vector3d,transformation: Transformation,
             mat: Material,  shadow: Boolean): MeshTriangle = new SmoothMeshTriangle(v1, v2, v3, uv1, uv2, uv3, normal, transformation, mat, shadow)
}

abstract class MeshTriangle(val v1: Vertex,
                            val v2: Vertex,
                            val v3: Vertex,
                            val uv1: Point2d,
                            val uv2: Point2d,
                            val uv3: Point2d,
                            val normal: Vector3d,
                            val transformation: Transformation,
                            mat: Material,
                            shadow: Boolean) extends FiniteGeometricObject(mat, shadow){
  protected def getBetaGammaT(rayLocal: Ray): (Double, Double, Double) = {
    val c: Double = rayLocal.direction.x
    val g: Double = rayLocal.direction.y
    val k: Double = rayLocal.direction.z

    val d: Double = v1.origin.x - rayLocal.origin.x
    val h: Double = v1.origin.y - rayLocal.origin.y
    val l: Double = v1.origin.z - rayLocal.origin.z

    val m: Double = f * k - g * j
    val n: Double = h * k - g * l
    val p: Double = f * l - h * j

    val q: Double = g * i - e * k
    val r: Double = e * l - h * i

    val inv_denom: Double = 1.0 / (a * m + b * q + c * s)

    val e1: Double = d * m - b * n - c * p
    val beta: Double = e1 * inv_denom

    val e2: Double = a * n + d * q + c * r
    val gamma: Double = e2 * inv_denom

    val e3: Double = a * p - b * r + d * s
    val t: Double = e3 * inv_denom
    (beta, gamma, t)
  }

  override def intersectDistance(ray: Ray): Option[Double] = {
    val rayLocal: Ray = this.transformation transformInv ray

    val (beta, gamma, t) = getBetaGammaT(rayLocal)

    if ((((beta < 0.0 || beta > 1.0) ||
       (gamma < 0.0 || gamma > 1.0)) ||
       (beta + gamma) > 1.0) ||
        t < Cons.kEpsilon)  None
    else Option(t)
  }

  protected val a: Double = v1.origin.x - v2.origin.x
  protected val e: Double = v1.origin.y - v2.origin.y
  protected val i: Double = v1.origin.z - v2.origin.z

  protected val b: Double = v1.origin.x - v3.origin.x
  protected val f: Double = v1.origin.y - v3.origin.y
  protected val j: Double = v1.origin.z - v3.origin.z

  protected val s: Double = e * j - f * i

  val boundingBox: AABBNode = {
    val transOriginVertex1: Point3d = transformation transform v1.origin
    val transOriginVertex2: Point3d = transformation transform v2.origin
    val transOriginVertex3: Point3d = transformation transform v3.origin

    val x0: Double = List(transOriginVertex1.x, transOriginVertex2.x, transOriginVertex3.x).min
    val x1: Double = List(transOriginVertex1.x, transOriginVertex2.x, transOriginVertex3.x).max

    val y0: Double = List(transOriginVertex1.y, transOriginVertex2.y, transOriginVertex3.y).min
    val y1: Double = List(transOriginVertex1.y, transOriginVertex2.y, transOriginVertex3.y).max

    val z0: Double = List(transOriginVertex1.z, transOriginVertex2.z, transOriginVertex3.z).min
    val z1: Double = List(transOriginVertex1.z, transOriginVertex2.z, transOriginVertex3.z).max
    AABBNode.leaf(new Point3d(x0, y0, z0), new Point3d(x1, y1, z1), this)
  }

  override def toString = "MeshTriangle: \n v1: " + v1 + "\n v2: " + v2 + "\n v3: " + v3 + "\nnormal: " + normal
}

class FlatMeshTriangle(u1: Vertex,
                       u2: Vertex,
                       u3: Vertex,
                       t1: Point2d,
                       t2: Point2d,
                       t3: Point2d,
                       n: Vector3d,
                       trans: Transformation,
                       tMat: Material,
                       shadow: Boolean) extends MeshTriangle(u1, u2, u3, t1, t2, t3, n, trans, tMat, shadow) {
  override def intersect(ray: Ray): Option[RayResult] = {
    val rayLocal: Ray = this.transformation transformInv ray
    val (beta, gamma, t) = getBetaGammaT(rayLocal)

    if ((((beta < 0.0 || beta > 1.0) ||
      (gamma < 0.0 || gamma > 1.0)) ||
      (beta + gamma) > 1.0) ||
      t < Cons.kEpsilon)  None
    else {
      val localHitPoint: Point3d = rayLocal.origin + rayLocal.direction * t
      val uv: Point2d = (uv1 * (1 - beta - gamma)) +
                        (uv2 * beta) +
                        (uv3 * gamma)

      Option(RayResult(localHitPoint,
        transformation transform localHitPoint,
        normalAt(localHitPoint, rayLocal.direction.inverted),
        uv,
        material,
        ray,
        t))
    }
  }

  def normalAt(p: Point3d, w0: Vector3d): Vector3d = {
    if ((normal dot w0) >= 0.0) transformation.transform(normal).normalized
    else transformation.transform(normal).normalized.inverted
  }
}

class SmoothMeshTriangle(u1: Vertex,
                         u2: Vertex,
                         u3: Vertex,
                         t1: Point2d,
                         t2: Point2d,
                         t3: Point2d,
                         n: Vector3d,
                         trans: Transformation,
                         tMat: Material,
                         shadow: Boolean) extends MeshTriangle(u1, u2, u3, t1, t2, t3, n, trans, tMat, shadow) {
  override def intersect(ray: Ray): Option[RayResult] = {
    val rayLocal: Ray = this.transformation transformInv ray
    val (beta, gamma, t) = getBetaGammaT(rayLocal)

    if ((((beta < 0.0 || beta > 1.0) ||
      (gamma < 0.0 || gamma > 1.0)) ||
      (beta + gamma) > 1.0) ||
      t < Cons.kEpsilon)  None
    else {
      val localHitPoint: Point3d = rayLocal.origin + rayLocal.direction * t

      val uv: Point2d = (uv1 * (1 - beta - gamma)) +
                        (uv2 * beta) +
                        (uv3 * gamma)

      val normalInterpolated: Vector3d = ((v1.normal * (1 - beta - gamma)) +
                                          (v2.normal * beta) +
                                          (v3.normal * gamma)).normalized
      val normalFinal = if ((normalInterpolated dot rayLocal.direction.inverted) >= 0.0) transformation.transform(normalInterpolated).normalized
                        else transformation.transform(normalInterpolated).normalized.inverted

      Option(RayResult(localHitPoint,
        transformation transform localHitPoint,
        normalFinal,
        uv,
        material,
        ray,
        t))
    }
  }
}