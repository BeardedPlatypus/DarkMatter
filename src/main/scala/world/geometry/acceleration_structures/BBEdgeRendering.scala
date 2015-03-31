package world.geometry.acceleration_structures

import com.beardedplatypus.math.Ray
import com.beardedplatypus.shading.RayResult
import com.beardedplatypus.world.geometry.acceleration_structures.AABBNode

/**
 * Created by Month on 10/03/2015.
 */
trait BBEdgeRendering extends AABBNode {
  abstract override def intersectDistance(ray: Ray): Option[Double]
  abstract override def intersect(ray: Ray): Option[RayResult]


}
