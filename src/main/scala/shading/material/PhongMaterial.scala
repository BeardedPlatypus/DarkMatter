package shading.material

import com.beardedplatypus.math.{Vector3d, Ray}
import com.beardedplatypus.shading.{Color, RayResult}
import com.beardedplatypus.shading.brdf.{GlossySpecularBRDF, LambertianBRDF}
import com.beardedplatypus.shading.material.Material
import com.beardedplatypus.world.{Light, Scene}

import scala.annotation.tailrec

class PhongMaterial(val ambientBRDF: LambertianBRDF,
                    val diffuseBRDF: LambertianBRDF,
                    val specularBRDF: GlossySpecularBRDF,
                    shadow: Boolean) extends Material(shadow) {
  override def shade(rayResult: RayResult,
                     scene: Scene): Color = {
    lazy val wo: Vector3d = rayResult.ray.direction.inverted.normalized
    lazy val ambientComp: Color = ambientBRDF.rho(rayResult, wo) * scene.ambientLight.L(rayResult.worldHitPoint)

    @tailrec
    def diffuseComponentShadow(lights: List[Light], acc: Color): Color = lights match {
      case Nil => acc
      case l :: ls => {
        lazy val wi: Vector3d = l.direction(rayResult.worldHitPoint).normalized
        lazy val ndotwi: Double = rayResult.normal dot wi
        lazy val diffComp: Color = (diffuseBRDF.f(rayResult, wi, wo) +
                                    specularBRDF.f(rayResult, wi, wo)) * l.L(rayResult.worldHitPoint) * ndotwi

        lazy val shadowRay: Ray = Ray(rayResult.worldHitPoint, l.direction(rayResult.worldHitPoint), rayResult.ray.sample)

        // FIXME: add stuff such that it doesn't use visibility when light does not casts shadow
        if (ndotwi > 0.0 && (!this.receivesShadow || (!l.castsShadow || scene.visible(shadowRay, l.distanceTo(rayResult.worldHitPoint))))) {
          diffuseComponentShadow(ls, acc + diffComp)
        } else diffuseComponentShadow(ls, acc)
      }
    }

    ambientComp + diffuseComponentShadow(scene.lights, Color.black)
  }
}