package com.beardedplatypus.main

import com.beardedplatypus.camera.PerspectiveCamera
import com.beardedplatypus.math.{Vector3d, Point3d, Transformation}
import com.beardedplatypus.sampling.SamplerStrategy
import com.beardedplatypus.shading.{Texture, Color}
import com.beardedplatypus.shading.brdf.{GlossySpecularBRDF, LambertianBRDF}
import com.beardedplatypus.shading.material._
import com.beardedplatypus.world.geometry.acceleration_structures.{SimpleList, AABBTree}
import com.beardedplatypus.world.geometry.objects.polygon._
import com.beardedplatypus.world.geometry.objects.primitives.FinitePlane
import com.beardedplatypus.world.light.{PlaneAreaLight, PointLight, AmbientLight, Light}
import com.beardedplatypus.world.Scene
import com.beardedplatypus.world.geometry._
import world.geometry.objects.FiniteGeometricObject
import world.geometry.objects.primitives.{Cube, Sphere}

/**
 * Created by Month on 24/02/2015.
 */
object SceneBuilder {
  def scene01(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    lazy val cameraPosition = Transformation.translation(0.0, 1.2, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1 = Transformation.identity //scale(1.0, 2.0, 1.0)
    val matte: Material = new PhongMaterial(LambertianBRDF(0.05, Color.white),
        LambertianBRDF(0.6, Color.white),
        new GlossySpecularBRDF(0.4, Color.white, 300),
        true)

    val t0: Double = System.nanoTime()
    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\teapot.obj", t1, matte, false, false)
    val bb = AABBTree.construct(s)
    val time1: Double = System.nanoTime()
    println("Total time building triangles: " + ((time1 - t0) / 1000000000.0))


    // lights
    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    println("here: " + s.length)
    println(bb)

    new Scene(perspectiveCamera,
      bb,
      ambientLight,
      lights,
      Color.gray)
  }

  def scene01noas(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    lazy val cameraPosition = Transformation.translation(0.0, 1.2, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1 = Transformation.identity //scale(1.0, 2.0, 1.0)
    val matte: Material = new PhongMaterial(LambertianBRDF(0.05, Color.white),
        LambertianBRDF(0.6, Color.white),
        new GlossySpecularBRDF(0.4, Color.white, 300),
        true)

    val t0: Double = System.nanoTime()
    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\teapot.obj", t1, matte, false, false)
    val bb = new SimpleList(s)
    val time1: Double = System.nanoTime()
    println("Total time building triangles: " + ((time1 - t0) / 1000000000.0))


    // lights
    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    println("here: " + s.length)
    println(bb)

    new Scene(perspectiveCamera,
      bb,
      ambientLight,
      lights,
      Color.gray)
  }


  def scene02(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    lazy val cameraPosition = Transformation.translation(0.0, 0.0, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1 = Transformation.identity //scale(1.0, 2.0, 1.0)
    val matte: Material = new NormalMaterial()

    val t0: Double = System.nanoTime()
    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\bunny.obj", t1, matte, false, false)
    val bb = AABBTree.construct(s)
    val time1: Double = System.nanoTime()
    println("Total time building triangles: " + ((time1 - t0) / 1000000000.0))


    // lights
    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    println("here: " + s.length)
    println(bb)

    new Scene(perspectiveCamera,
      bb,
      ambientLight,
      lights,
      Color.gray)
  }

  def scene04(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    lazy val cameraPosition = Transformation.translation(0.0, 0.0, -1.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1 = Transformation.scale(1.5, 1.5, 1.5)
    val matte: Material = new PhongMaterial(LambertianBRDF(0.0, Color.white),
                                            LambertianBRDF(0.5, Color.blue),
      new GlossySpecularBRDF(0.5, Color.white, 1000),
      true)

    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\bunny.obj", t1, matte, false, false)
    val bb = AABBTree.construct(s)

    // lights
    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    println("here: " + s.length)
    println(bb)

    new Scene(perspectiveCamera,
      bb,
      ambientLight,
      lights,
      Color.gray)
  }

  def scene05(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    lazy val cameraPosition = Transformation.translation(0.0, 0.0, -0.65) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1 = Transformation.identity //scale(1.0, 2.0, 1.0)
    val matte: Material = new LambertMaterial(LambertianBRDF(0.08, Color.white),
                                              LambertianBRDF(0.92, Color.white),
                                              true)
    val t0: Double = System.nanoTime()
    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\buddha.obj", t1, matte, false, false)
    val time1: Double = System.nanoTime()
    println("Total time building triangles: " + ((time1 - t0) / 1000000000.0))

    println("starting building bounding box ... ")
    val t2 = System.nanoTime()
    val bb = AABBTree.construct(s)
    val t3 = System.nanoTime()

    println("done")
    println("Total time building bounding box: " + ((t3 - t2) / 1000000000.0))

    // lights
    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    println("here: " + s.length)
    println(bb)

    new Scene(perspectiveCamera,
      bb,
      ambientLight,
      lights,
      Color.gray)
  }

  def sceneTexture1(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    lazy val cameraPosition = Transformation.translation(0.0, 0.5, -1.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1 = Transformation.scale(1.5, 1.5, 1.5)

    val texture = Texture.image("..\\obj\\apple_texture.jpg")
    val matte: Material = new LambertMaterial(LambertianBRDF(0.1, texture),
                                              LambertianBRDF(1.0, texture),
                                              true)

    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\apple.obj", t1, matte, false, false)
    val bb = AABBTree.construct(s)

    // lights
    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    println("here: " + s.length)
    println(bb)

    new Scene(perspectiveCamera,
      bb,
      ambientLight,
      lights,
      Color.gray)
  }


  def sceneTexture2(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    val cameraPosition = Transformation.translation(0.0, 0.5, -1.5) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1 = Transformation.rotate("y", 60) //Transformation.scale(1.5, 1.5, 1.5)

    val texture = Texture.image("..\\obj\\house_texture.jpg")
    val matte: Material = new LambertMaterial(LambertianBRDF(0.1, texture),
      LambertianBRDF(1.0, texture),
      true)

    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\house.obj", t1, matte, false, true)
    val bb = AABBTree.construct(s)

    // lights
    val pl1 = new Point3d(0.0, 0.0, -10.0)
    val l1 = new PointLight(Color.white, 2, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    println("here: " + s.length)
    println(bb)

    new Scene(perspectiveCamera,
      bb,
      ambientLight,
      lights,
      Color.gray)
  }

  def sceneAreaLightTest1(width: Int, height: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 0.0, -4.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    val t1 = Transformation.translation(0.5, 0.0, 0.0) * Transformation.rotate("x", -90)
    val t2 = Transformation.translation(-0.5, 0.0, 0.0) * Transformation.rotate("x", 90)

    val mat = new EmissiveMaterial(1.0, Color.white)

    val plane1 = new PlaneAreaLight(t1, mat, 1, SamplerStrategy.Constant, false)
    val plane2 = new PlaneAreaLight(t2, mat, 1, SamplerStrategy.Constant, false)

    val bb = new SimpleList(List(plane1, plane2))
    val ambientLight = new AmbientLight(Color.white, 0.0)

    new Scene(perspectiveCamera, bb, ambientLight, Nil, Color.gray)
  }

  def sceneAreaLightTest2(width: Int, height: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 0.0, -4.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    val t1 = Transformation.translation(0.0, 3.0, 0.0) * Transformation.scale(2, 2, 2) * Transformation.rotate("x", 180)
    val t2 = Transformation.translation(0.0, -2.0, 0.0) * Transformation.scale(5.0, 5.0, 5.0)

    val mat = new EmissiveMaterial(1.0, Color.white)
    val matPhong: Material = new PhongMaterial(LambertianBRDF(0.05, Color.white),
                                               LambertianBRDF(0.6, Color.white),
                                               new GlossySpecularBRDF(0.4, Color.white, 300),
                                               true)

    val matLambert1 = new LambertMaterial(LambertianBRDF(0.08, Color.white),
                                          LambertianBRDF(0.92, Color.white),
                                          true)
    val matLambert2 = new LambertMaterial(LambertianBRDF(0.08, Color.white),
                                          LambertianBRDF(0.92, Color.white),
                                          true)



    val plane1 = new PlaneAreaLight(t1, mat, 5, SamplerStrategy.Jittered, true)
    val sphere = new Sphere(Transformation.identity, matLambert1, true)
    val plane = new FinitePlane(t2, matLambert2, true)


    val bb = new SimpleList(List(plane1, plane, sphere))
    val ambientLight = new AmbientLight(Color.white, 0.0)
    val lights: List[Light] = List(plane1)
    new Scene(perspectiveCamera, bb, ambientLight, lights, Color.gray)
  }


}