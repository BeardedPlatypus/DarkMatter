package com.beardedplatypus.main

import com.beardedplatypus.camera.PerspectiveCamera
import com.beardedplatypus.math.{Vector3d, Point3d, Transformation}
import com.beardedplatypus.sampling.SamplerStrategy
import com.beardedplatypus.shading.{Texture, Color}
import com.beardedplatypus.shading.brdf.{GlossySpecularBRDF, LambertianBRDF}
import com.beardedplatypus.shading.material._
import com.beardedplatypus.world.geometry.acceleration_structures.{AccelerationStructure, SimpleList, AABBTree}
import com.beardedplatypus.world.geometry.objects.polygon._
import com.beardedplatypus.world.geometry.objects.primitives.{FinitePlane, Sphere, Cylinder, Cube}
import com.beardedplatypus.world.light.{PlaneAreaLight, PointLight, AmbientLight, Light}
import com.beardedplatypus.world.Scene
import com.beardedplatypus.world.geometry.objects.FiniteGeometricObject

object SceneBuilder {
  // Scene map function TODO: create nicer version, possibly create a format to load scenes
  def getScene(name: String, width: Int, height: Int): Scene = name match {
    case "cubeTest" => cubeTest(width, height)
    case "cylinderTest" => cylinderTest(width, height)
    case "sphereTest" => sphereTest(width, height)
    case "meshTest1" => meshTest1(width, height)
    case "meshTest2" => meshTest2(width, height)
    case "textureTest1" => textureTest1(width, height)
    case "textureTest2" => textureTest2(width, height)
    case "areaLightTest" => areaLightTest(width, height)
    case _ => throw new IllegalArgumentException()
  }

  // -----------------------------------------------------------------------------------------
  // Scenes
  def cubeTest(width: Int, height: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 1.2, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height,
                                                                 cameraPosition,
                                                                 Vector3d(0.0, 0.0, 1.0),
                                                                 Vector3d(0.0, 1.0, 0.0),
                                                                 90)

    val t1: Transformation = Transformation.translation(1.0, 1.0, 0.0)
    val t2: Transformation = Transformation.translation(-1.0, 1.0, 0.0)
    val t3: Transformation = Transformation.translation(1.0, -1.0, 0.0)
    val t4: Transformation = Transformation.translation(-1.0, -1.0, 0.0)
    val t5: Transformation = Transformation.translation(1.0, 3.0, 0.0)
    val t6: Transformation = Transformation.translation(-1.0, 3.0, 0.0)



    val r1: Transformation = Transformation.rotate("x", 45)
    val r2: Transformation = Transformation.rotate("x", 60)

    val r3: Transformation = Transformation.rotate("y", 45)
    val r4: Transformation = Transformation.rotate("y", 60)

    val r5: Transformation = Transformation.rotate("z", 45)
    val r6: Transformation = Transformation.rotate("z", 60)


    val matte: Material = new PhongMaterial(LambertianBRDF(0.05, Color.white),
      LambertianBRDF(0.6, Color.white),
      new GlossySpecularBRDF(0.4, Color.white, 300),
      true)

    val c1 = new Cube(t1 * r1, matte, false)
    val c2 = new Cube(t2 * r2, matte, false)
    val c3 = new Cube(t3 * r3, matte, false)
    val c4 = new Cube(t4 * r4, matte, false)
    val c5 = new Cube(t5 * r5, matte, false)
    val c6 = new Cube(t6 * r6, matte, false)


    val accStruc: AccelerationStructure = new SimpleList(List(c1, c2, c3, c4, c5, c6))

    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    new Scene(perspectiveCamera,
      accStruc,
      ambientLight,
      lights,
      Color.gray)

  }

  def cylinderTest(width: Int, height: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 1.2, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height,
      cameraPosition,
      Vector3d(0.0, 0.0, 1.0),
      Vector3d(0.0, 1.0, 0.0),
      90)

    val t1: Transformation = Transformation.translation(1.0, 1.0, 0.0)
    val t2: Transformation = Transformation.translation(-1.0, 1.0, 0.0)
    val t3: Transformation = Transformation.translation(1.0, -1.0, 0.0)
    val t4: Transformation = Transformation.translation(-1.0, -1.0, 0.0)
    val t5: Transformation = Transformation.translation(1.0, 3.0, 0.0)
    val t6: Transformation = Transformation.translation(-1.0, 3.0, 0.0)



    val r1: Transformation = Transformation.rotate("x", 45)
    val r2: Transformation = Transformation.rotate("x", 60)

    val r3: Transformation = Transformation.rotate("y", 45)
    val r4: Transformation = Transformation.rotate("y", 60)

    val r5: Transformation = Transformation.rotate("z", 45)
    val r6: Transformation = Transformation.rotate("z", 60)


    val matte: Material = new PhongMaterial(LambertianBRDF(0.05, Color.white),
      LambertianBRDF(0.6, Color.white),
      new GlossySpecularBRDF(0.4, Color.white, 300),
      true)

    val c1 = new Cylinder(t1 * r1, matte, false)
    val c2 = new Cylinder(t2 * r2, matte, false)
    val c3 = new Cylinder(t3 * r3, matte, false)
    val c4 = new Cylinder(t4 * r4, matte, false)
    val c5 = new Cylinder(t5 * r5, matte, false)
    val c6 = new Cylinder(t6 * r6, matte, false)


    val accStruc: AccelerationStructure = new SimpleList(List(c1, c2, c3, c4, c5, c6))

    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    new Scene(perspectiveCamera,
      accStruc,
      ambientLight,
      lights,
      Color.gray)
  }

  def sphereTest(width: Int, height: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 1.2, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height,
      cameraPosition,
      Vector3d(0.0, 0.0, 1.0),
      Vector3d(0.0, 1.0, 0.0),
      90)

    val t1: Transformation = Transformation.translation(1.0, 1.0, 0.0)
    val t2: Transformation = Transformation.translation(-1.0, 1.0, 0.0)
    val t3: Transformation = Transformation.translation(1.0, -1.0, 0.0)
    val t4: Transformation = Transformation.translation(-1.0, -1.0, 0.0)
    val t5: Transformation = Transformation.translation(1.0, 3.0, 0.0)
    val t6: Transformation = Transformation.translation(-1.0, 3.0, 0.0)



    val r1: Transformation = Transformation.rotate("x", 45)
    val r2: Transformation = Transformation.rotate("x", 60)

    val r3: Transformation = Transformation.rotate("y", 45)
    val r4: Transformation = Transformation.rotate("y", 60)

    val r5: Transformation = Transformation.rotate("z", 45)
    val r6: Transformation = Transformation.rotate("z", 60)


    val matte: Material = new PhongMaterial(LambertianBRDF(0.05, Color.white),
      LambertianBRDF(0.6, Color.white),
      new GlossySpecularBRDF(0.4, Color.white, 300),
      true)

    val c1 = new Sphere(t1 * r1, matte, false)
    val c2 = new Sphere(t2 * r2, matte, false)
    val c3 = new Sphere(t3 * r3, matte, false)
    val c4 = new Sphere(t4 * r4, matte, false)
    val c5 = new Sphere(t5 * r5, matte, false)
    val c6 = new Sphere(t6 * r6, matte, false)


    val accStruc: AccelerationStructure = new SimpleList(List(c1, c2, c3, c4, c5, c6))

    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    new Scene(perspectiveCamera,
      accStruc,
      ambientLight,
      lights,
      Color.gray)
  }

  def meshTest1(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    lazy val cameraPosition = Transformation.translation(0.0, 1.2, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1: Transformation = Transformation.translation(1.0, 1.0, 0.0)
    val t2: Transformation = Transformation.translation(-1.0, 1.0, 0.0)
    val t3: Transformation = Transformation.translation(1.0, -1.0, 0.0)
    val t4: Transformation = Transformation.translation(-1.0, -1.0, 0.0)
    val t5: Transformation = Transformation.translation(1.0, 3.0, 0.0)
    val t6: Transformation = Transformation.translation(-1.0, 3.0, 0.0)

    val r1: Transformation = Transformation.rotate("x", 45)
    val r2: Transformation = Transformation.rotate("x", 60)

    val r3: Transformation = Transformation.rotate("y", 45)
    val r4: Transformation = Transformation.rotate("y", 60)

    val r5: Transformation = Transformation.rotate("z", 45)
    val r6: Transformation = Transformation.rotate("z", 60)

    val matte: Material = new PhongMaterial(LambertianBRDF(0.05, Color.white),
        LambertianBRDF(0.6, Color.white),
        new GlossySpecularBRDF(0.4, Color.white, 300),
        true)

    val t0: Double = System.nanoTime()
    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\teapot.obj", Transformation.scale(0.5, 0.5, 0.5), matte, false, false)
    val bb = AABBTree.construct(s)

    val c1 = new Mesh(bb, t1 * r1, matte, false)
    val c2 = new Mesh(bb, t2 * r2, matte, false)
    val c3 = new Mesh(bb, t3 * r3, matte, false)
    val c4 = new Mesh(bb, t4 * r4, matte, false)
    val c5 = new Mesh(bb, t5 * r5, matte, false)
    val c6 = new Mesh(bb, t6 * r6, matte, false)

    val accStruc: AccelerationStructure = new SimpleList(List(c1, c2, c3, c4, c5, c6))

    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    new Scene(perspectiveCamera,
      accStruc,
      ambientLight,
      lights,
      Color.gray)
  }

  def meshTest2(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    val cameraPosition = Transformation.translation(0.0, 1.2, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1: Transformation = Transformation.translation(1.0, 1.0, 0.0)
    val t2: Transformation = Transformation.translation(-1.0, 1.0, 0.0)
    val t3: Transformation = Transformation.translation(1.0, -1.0, 0.0)
    val t4: Transformation = Transformation.translation(-1.0, -1.0, 0.0)
    val t5: Transformation = Transformation.translation(1.0, 3.0, 0.0)
    val t6: Transformation = Transformation.translation(-1.0, 3.0, 0.0)

    val r1: Transformation = Transformation.rotate("x", 45)
    val r2: Transformation = Transformation.rotate("x", 60)

    val r3: Transformation = Transformation.rotate("y", 45)
    val r4: Transformation = Transformation.rotate("y", 60)

    val r5: Transformation = Transformation.rotate("z", 45)
    val r6: Transformation = Transformation.rotate("z", 60)

    val matte: Material = new PhongMaterial(LambertianBRDF(0.05, Color.white),
      LambertianBRDF(0.6, Color.white),
      new GlossySpecularBRDF(0.4, Color.white, 300),
      true)

    val t0: Double = System.nanoTime()
    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\bunny.obj", Transformation.scale(0.5, 0.5, 0.5), matte, false, false)
    val bb = AABBTree.construct(s)

    val c1 = new Mesh(bb, t1 * r1, matte, false)
    val c2 = new Mesh(bb, t2 * r2, matte, false)
    val c3 = new Mesh(bb, t3 * r3, matte, false)
    val c4 = new Mesh(bb, t4 * r4, matte, false)
    val c5 = new Mesh(bb, t5 * r5, matte, false)
    val c6 = new Mesh(bb, t6 * r6, matte, false)

    val accStruc: AccelerationStructure = new SimpleList(List(c1, c2, c3, c4, c5, c6))

    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    new Scene(perspectiveCamera,
      accStruc,
      ambientLight,
      lights,
      Color.gray)
  }

  def textureTest1(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    val cameraPosition = Transformation.translation(0.0, 1.2, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1: Transformation = Transformation.translation(1.0, 1.0, 0.0)
    val t2: Transformation = Transformation.translation(-1.0, 1.0, 0.0)
    val t3: Transformation = Transformation.translation(1.0, -1.0, 0.0)
    val t4: Transformation = Transformation.translation(-1.0, -1.0, 0.0)
    val t5: Transformation = Transformation.translation(1.0, 3.0, 0.0)
    val t6: Transformation = Transformation.translation(-1.0, 3.0, 0.0)

    val r1: Transformation = Transformation.rotate("x", 45)
    val r2: Transformation = Transformation.rotate("x", 60)

    val r3: Transformation = Transformation.rotate("y", 45)
    val r4: Transformation = Transformation.rotate("y", 60)

    val r5: Transformation = Transformation.rotate("z", 45)
    val r6: Transformation = Transformation.rotate("z", 60)

    val texture = Texture.image("..\\obj\\house_texture.jpg")
    val matte: Material = new LambertMaterial(LambertianBRDF(0.1, texture),
      LambertianBRDF(1.0, texture),
      true)

    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\house.obj", Transformation.scale(1.5, 1.5, 1.5), matte, false, true)
    val bb = AABBTree.construct(s)

    val c1 = new Mesh(bb, t1 * r1, matte, false)
    val c2 = new Mesh(bb, t2 * r2, matte, false)
    val c3 = new Mesh(bb, t3 * r3, matte, false)
    val c4 = new Mesh(bb, t4 * r4, matte, false)
    val c5 = new Mesh(bb, t5 * r5, matte, false)
    val c6 = new Mesh(bb, t6 * r6, matte, false)

    val accStruc: AccelerationStructure = new SimpleList(List(c1, c2, c3, c4, c5, c6))

    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    new Scene(perspectiveCamera,
      accStruc,
      ambientLight,
      lights,
      Color.gray)
  }

  def textureTest2(width: Int, height: Int): Scene = {
    // basicSphereMatteSinglePointLightNoShadow
    // Camera
    val cameraPosition = Transformation.translation(0.0, 1.2, -5.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    // Shapes
    //val t1 = Transformation.identity
    val t1: Transformation = Transformation.translation(1.0, 1.0, 0.0)
    val t2: Transformation = Transformation.translation(-1.0, 1.0, 0.0)
    val t3: Transformation = Transformation.translation(1.0, -1.0, 0.0)
    val t4: Transformation = Transformation.translation(-1.0, -1.0, 0.0)
    val t5: Transformation = Transformation.translation(1.0, 3.0, 0.0)
    val t6: Transformation = Transformation.translation(-1.0, 3.0, 0.0)

    val r1: Transformation = Transformation.rotate("x", 45)
    val r2: Transformation = Transformation.rotate("x", 60)

    val r3: Transformation = Transformation.rotate("y", 45)
    val r4: Transformation = Transformation.rotate("y", 60)

    val r5: Transformation = Transformation.rotate("z", 45)
    val r6: Transformation = Transformation.rotate("z", 60)

    val texture = Texture.image("..\\obj\\apple_texture.jpg")
    val matte: Material = new LambertMaterial(LambertianBRDF(0.1, texture),
      LambertianBRDF(1.0, texture),
      true)

    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\apple.obj", Transformation.scale(1.5, 1.5, 1.5), matte, false, true)
    val bb = AABBTree.construct(s)

    val c1 = new Mesh(bb, t1 * r1, matte, false)
    val c2 = new Mesh(bb, t2 * r2, matte, false)
    val c3 = new Mesh(bb, t3 * r3, matte, false)
    val c4 = new Mesh(bb, t4 * r4, matte, false)
    val c5 = new Mesh(bb, t5 * r5, matte, false)
    val c6 = new Mesh(bb, t6 * r6, matte, false)

    val accStruc: AccelerationStructure = new SimpleList(List(c1, c2, c3, c4, c5, c6))

    val pl1 = new Point3d(0.0, 10.0, -5.0)
    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = List(l1)

    val ambientLight = new AmbientLight(Color.white, 0.5)

    new Scene(perspectiveCamera,
      accStruc,
      ambientLight,
      lights,
      Color.gray)
  }

  def areaLightTest(width: Int, height: Int): Scene = {
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