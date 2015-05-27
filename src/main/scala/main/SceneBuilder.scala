package com.beardedplatypus.main

import com.beardedplatypus.camera.PerspectiveCamera
import com.beardedplatypus.math.{OrthonormalBasis, Vector3d, Point3d, Transformation}
import com.beardedplatypus.sampling.{Sampler, SamplerStrategy}
import com.beardedplatypus.shading.{Texture, Color}
import com.beardedplatypus.shading.brdf.{SpecularBRDF, PerfectSpecularBRDF, GlossySpecularBRDF, LambertianBRDF}
import com.beardedplatypus.shading.material._
import com.beardedplatypus.world.geometry.acceleration_structures.{AccelerationStructure, SimpleList, AABBTree}
import com.beardedplatypus.world.geometry.objects.polygon._
import com.beardedplatypus.world.geometry.objects.primitives.{FinitePlane, Sphere, Cylinder, Cube}
import com.beardedplatypus.world.light._
import com.beardedplatypus.world.Scene
import com.beardedplatypus.world.geometry.objects.{GeometricObject, FiniteGeometricObject}

import scala.util.Random

object SceneBuilder {
  // Scene map function TODO: create nicer version, possibly create a format to load scenes
  def getScene(name: String, width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = name match {
    case "cubeTest" => cubeTest(width, height, maxDepthDirect, maxDepthIndirect)
    case "cylinderTest" => cylinderTest(width, height, maxDepthDirect, maxDepthIndirect)
    case "sphereTest" => sphereTest(width, height, maxDepthDirect, maxDepthIndirect)
    case "meshTest1" => meshTest1(width, height, maxDepthDirect, maxDepthIndirect)
    case "meshTest2" => meshTest2(width, height, maxDepthDirect, maxDepthIndirect)
    case "textureTest1" => textureTest1(width, height, maxDepthDirect, maxDepthIndirect)
    case "textureTest2" => textureTest2(width, height, maxDepthDirect, maxDepthIndirect)
    case "areaLightTest" => areaLightTest(width, height, maxDepthDirect, maxDepthIndirect)
    case "hemisphereTest" => hemisphereTest(width, height, maxDepthDirect, maxDepthIndirect)
    case "ambientOcclusionTest" => ambientOcclusionTest(width, height, maxDepthDirect, maxDepthIndirect)
    case "globalIlluminationTest1" => globalIlluminationTest1(width, height, maxDepthDirect, maxDepthIndirect)
    case "globalIlluminationTest2" => globalIlluminationTest2(width, height, maxDepthDirect, maxDepthIndirect)
    case "cornellBox" => cornellBox(width, height, maxDepthDirect, maxDepthIndirect)
    case "cornellBox2" => cornellBox2(width, height, maxDepthDirect, maxDepthIndirect)
    case "cornellBoxReflectiveSphere" => cornellBoxReflectiveSphere(width, height, maxDepthDirect, maxDepthIndirect)
    case "cornellBoxGlossyTest" => cornellBoxGlossyTest(width, height, maxDepthDirect, maxDepthIndirect)
    case _ => throw new IllegalArgumentException()
  }

  // -----------------------------------------------------------------------------------------
  // Scenes
  def cubeTest(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
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
      Color.gray,
      maxDepthDirect,
      maxDepthIndirect)
  }

  def cylinderTest(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
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
      Color.gray,
      maxDepthDirect,
      maxDepthIndirect)
  }

  def sphereTest(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
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
      Color.gray,
      maxDepthDirect,
      maxDepthIndirect)
  }

  def meshTest1(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
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
      Color.gray,
      maxDepthDirect,
      maxDepthIndirect)
  }

  def meshTest2(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
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
      Color.gray,
      maxDepthDirect,
      maxDepthIndirect)
  }

  def textureTest1(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
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
      Color.gray,
      maxDepthDirect,
      maxDepthIndirect)
  }

  def textureTest2(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
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
      Color.gray,
      maxDepthDirect,
      maxDepthIndirect)
  }

  def areaLightTest(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 0.0, -4.0) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
      90)

    val t1 = Transformation.translation(0.0, 3.0, 0.0) * Transformation.scale(2, 2, 2) * Transformation.rotate("x", 180)
    val t2 = Transformation.translation(0.0, -2.0, 0.0) * Transformation.scale(5.0, 5.0, 5.0)

    val mat = new EmissiveMaterial(1.0, Color.white, true)
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
    new Scene(perspectiveCamera, bb, ambientLight, lights, Color.gray, maxDepthDirect, maxDepthIndirect)
  }

  def hemisphereTest(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 2.0, -3.0) transform Point3d.origin
    val cameraRot = Transformation.rotate("x", 45.0)

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      cameraRot transform Vector3d(0.0, 0.0, 1.0), cameraRot transform Vector3d(0.0, 1.0, 0.0),
      90)

    val t1 = Transformation.translation(0.0, 3.0, 0.0) * Transformation.scale(2, 2, 2) * Transformation.rotate("x", 180)
    val t2 = Transformation.translation(0.0, -2.0, 0.0) * Transformation.scale(5.0, 5.0, 5.0)

    val mat = new EmissiveMaterial(0.5, Color.white, true)
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
    val tScale = Transformation.scale(0.05, 0.05, 0.05)
    val tRot = Transformation.rotate("x", -90.0)

    val spheres: List[GeometricObject] = (for(p <- Sampler.generateSamplesHemisphere(10, SamplerStrategy.Jittered, 0)) yield {
      val t = Transformation.translation(p.x, p.y, p.z)
      new Sphere(tRot * t * tScale, matLambert1, true)
    }).toList

    val sphere = new Sphere(Transformation.identity, matLambert1, true)
    val plane = new FinitePlane(Transformation.scale(3.0, 3.0, 3.0), matLambert2, true)

    val bb = new SimpleList(sphere :: plane1 :: plane :: spheres)
    val ambientLight = new AmbientLight(Color.white, 0.0)
    val lights: List[Light] = List(plane1)
    new Scene(perspectiveCamera, bb, ambientLight, lights, Color.gray, maxDepthDirect, maxDepthIndirect)
  }

  def ambientOcclusionTest(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 2.5, -2.0) transform Point3d.origin
    val cameraRot = Transformation.rotate("x", 35.0)

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      cameraRot transform Vector3d(0.0, 0.0, 1.0), cameraRot transform Vector3d(0.0, 1.0, 0.0),
      100)

    val matLambert2 = new LambertMaterial(LambertianBRDF(0.08, Color.white),
      LambertianBRDF(0.92, Color.white),
      true)

    val matte: Material = new PhongMaterial(LambertianBRDF(0.05, Color.white),
      LambertianBRDF(0.9, Color.white),
      new GlossySpecularBRDF(0.1, Color.white, 300),
      true)

    val plane = new FinitePlane(Transformation.scale(3.0, 3.0, 3.0), matLambert2, true)
    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\bunny.obj", Transformation.scale(0.5, 0.5, 0.5), matte, true, false)
    val bb = AABBTree.construct(s)
    val c1 = new Mesh(bb, Transformation.rotate("y", 190.0), matte, true)

    val accStruc: AccelerationStructure = new SimpleList(List(c1, plane))

    //val pl1 = new Point3d(0.0, 10.0, -5.0)
    //val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
    val lights: List[Light] = Nil

    val ambientLight = new AmbientOccluder(Color.white, 5.0, 0.5, accStruc, 8, SamplerStrategy.Jittered)

    new Scene(perspectiveCamera,
              accStruc,
              ambientLight,
              lights,
              Color.gray,
              maxDepthDirect,
              maxDepthIndirect)
  }

  def globalIlluminationTest1(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 2.0, -3.0) transform Point3d.origin
    val cameraRot = Transformation.rotate("x", 45.0)

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      cameraRot transform Vector3d(0.0, 0.0, 1.0), cameraRot transform Vector3d(0.0, 1.0, 0.0),
      90)

    val t1 = Transformation.translation(0.0, 3.0, 0.0) * Transformation.scale(2, 2, 2) * Transformation.rotate("x", 180)
    val t2 = Transformation.translation(0.0, -2.0, 0.0) * Transformation.scale(5.0, 5.0, 5.0)


    val matLambert1 = new LambertMaterial(LambertianBRDF(0.00, Color.white),
      LambertianBRDF(1.0, Color.white),
      true)
    val matLambert2 = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color.green),
      true)

    val tScale = Transformation.scale(0.05, 0.05, 0.05)
    val tRot = Transformation.rotate("x", -90.0)

    val spheres: List[GeometricObject] = (for(p <- Sampler.generateSamplesHemisphere(10, SamplerStrategy.Jittered, 0)) yield {
      val t = Transformation.translation(p.x, p.y, p.z)
      new Sphere(tRot * t * tScale, matLambert1, true)
    }).toList

    val sphere = new Sphere(Transformation.identity, matLambert1, true)
    val plane = new FinitePlane(Transformation.scale(3.0, 3.0, 3.0), matLambert2, true)

    val bb = new SimpleList(sphere :: plane :: spheres)
    val ambientLight = new AmbientLight(Color.white, 0.0)
    new Scene(perspectiveCamera, bb, ambientLight, Nil, Color.gray, maxDepthDirect, maxDepthIndirect)
  }

  def globalIlluminationTest2(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
    val cameraPosition = Transformation.translation(0.0, 3.0, -3.0) transform Point3d.origin
    val cameraRot = Transformation.rotate("x", 45.0)

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      cameraRot transform Vector3d(0.0, 0.0, 1.0), cameraRot transform Vector3d(0.0, 1.0, 0.0),
      90)

    val t1 = Transformation.translation(0.0, 3.0, 0.0) * Transformation.scale(2, 2, 2) * Transformation.rotate("x", 180)
    val t2 = Transformation.translation(0.0, -2.0, 0.0) * Transformation.scale(5.0, 5.0, 5.0)


    val matLambert1 = new LambertMaterial(LambertianBRDF(0.00, Color.white),
      LambertianBRDF(1.0, Color.white),
      true)
    val matLambert2 = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color.green),
      true)
    val emitLight = new EmissiveMaterial(1.0, Color.white, true)


    val mats = Vector(new LambertMaterial(LambertianBRDF(0.0, Color.white),
                                          LambertianBRDF(1.0, Color.green),
                                          true),
                      new LambertMaterial(LambertianBRDF(0.0, Color.white),
                                          LambertianBRDF(1.0, Color.blue),
                                          true),
                      new LambertMaterial(LambertianBRDF(0.0, Color.white),
                                          LambertianBRDF(1.0, Color.red),
                                          true),
                      new LambertMaterial(LambertianBRDF(0.0, Color.white),
                                          LambertianBRDF(1.0, Color.yellow),
                                          true),
                      new LambertMaterial(LambertianBRDF(0.0, Color.white),
                                          LambertianBRDF(1.0, Color.orange),
                                          true),
                      new LambertMaterial(LambertianBRDF(0.0, Color.white),
                                          LambertianBRDF(1.0, Color.magenta),
                                          true))

    val tScale = Transformation.scale(0.05, 0.05, 0.05)
    val tRot = Transformation.rotate("x", -90.0)

    val rand = new Random()

    val spheres: List[GeometricObject] = (for(p <- Sampler.generateSamplesHemisphere(10, SamplerStrategy.Jittered, 0)) yield {
      val t = Transformation.translation(p.x, p.y, p.z)
      new Sphere(tRot * t * tScale, mats(rand.nextInt(6)), true)
    }).toList

    val sphere = new Sphere(Transformation.identity, matLambert1, true)

    val lightSphere = new Sphere(Transformation.translation(0.0,  2, 0.0) * Transformation.scale(0.2, 0.2, 0.2), emitLight, true)

    val plane = new FinitePlane(Transformation.scale(3.0, 3.0, 3.0), matLambert2, true)

    val bb = new SimpleList(sphere :: plane :: lightSphere :: spheres)
    val ambientLight = new AmbientLight(Color.white, 0.0)
    new Scene(perspectiveCamera, bb, ambientLight, Nil, Color(0.4, 0.4, 0.4), maxDepthDirect, maxDepthIndirect)

  }

  def cornellBox(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
    val cameraPosition = Transformation.translation(0, 2.73, -7.2) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0), 60)

    val whiteMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
                                       LambertianBRDF(1.0, Color(0.9, 0.8, 0.6)),
                                       true)

    val greenMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
                                       LambertianBRDF(1.0, Color(0.0, 0.8, 0.0)),
                                       true)

    val redMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
                                     LambertianBRDF(1.0, Color(0.8, 0.0, 0.0)),
                                     true)

    val lightMat = new EmissiveMaterial(3.0, Color.white, true)//Color(1.0, 0.8, 0.6), true)

    val floor = new FinitePlane(Transformation.scale(5.56, 1.0, 5.56), whiteMat, true)
    val ceiling = new FinitePlane(Transformation.translation(0.0, 5.488, 0.0) * Transformation.rotate("x", 180.0) * Transformation.scale(5.56, 1.0, 5.56), whiteMat, true)
    val backWall = new FinitePlane(Transformation.translation(0.0, 2.744, 2.78) * Transformation.rotate("x", -90.0) * Transformation.scale(5.56, 1.0, 5.488), whiteMat, true)
    val redWall = new FinitePlane(Transformation.translation(2.78, 2.744, 0.0) * Transformation.rotate("z", 90.0) * Transformation.scale(5.488, 1.0, 5.56), redMat, true)
    val greenWall = new FinitePlane(Transformation.translation(-2.78, 2.744, 0.0) * Transformation.rotate("z", -90.0) * Transformation.scale(5.488, 1.0, 5.56), greenMat, true)
    val light = new PlaneAreaLight(Transformation.translation(0.0, 5.48799, 0.0) * Transformation.rotate("x", 180) * Transformation.scale(1.2, 1.0, 1.2), lightMat, 3, SamplerStrategy.Jittered, true)

    val shortBox = new Cube(Transformation.translation(1.0, 0.825, -1.0) * Transformation.rotate("y", 15.0) * Transformation.scale(1.65, 1.65, 1.65), whiteMat, true)
    val tallBox = new Cube(Transformation.translation(-1.1, 1.65, 1.1) * Transformation.rotate("y", 75.0) * Transformation.scale(1.65, 3.3, 1.65), whiteMat, true)

    val bb = new SimpleList(List(floor, ceiling, backWall, redWall, greenWall, light, shortBox, tallBox))
    val ambientLight = new AmbientLight(Color.white, 0.0)
    new Scene(perspectiveCamera, bb, ambientLight, List(light), Color(0.0, 0.0, 0.0), maxDepthDirect, maxDepthIndirect)
  }

  def cornellBox2(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
    val cameraPosition = Transformation.translation(0, 2.73, -7.2) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0), 60)

    val whiteMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color(0.9, 0.8, 0.6)),
      true)

    val greenMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color(0.0, 0.8, 0.0)),
      true)

    val redMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color(0.8, 0.0, 0.0)),
      true)

    val lightMat = new EmissiveMaterial(0.75, Color(1.0, 0.8, 0.6), true)

    val floor = new FinitePlane(Transformation.scale(5.56, 1.0, 5.56), whiteMat, true)
    val ceiling = new FinitePlane(Transformation.translation(0.0, 5.488, 0.0) * Transformation.rotate("x", 180.0) * Transformation.scale(5.56, 1.0, 5.56), whiteMat, true)
    val backWall = new FinitePlane(Transformation.translation(0.0, 2.744, 2.78) * Transformation.rotate("x", -90.0) * Transformation.scale(5.56, 1.0, 5.488), whiteMat, true)
    val redWall = new FinitePlane(Transformation.translation(2.78, 2.744, 0.0) * Transformation.rotate("z", 90.0) * Transformation.scale(5.488, 1.0, 5.56), redMat, true)
    val greenWall = new FinitePlane(Transformation.translation(-2.78, 2.744, 0.0) * Transformation.rotate("z", -90.0) * Transformation.scale(5.488, 1.0, 5.56), greenMat, true)
    val light = new PlaneAreaLight(Transformation.translation(0.0, 5.48799, 0.0) * Transformation.rotate("x", 180) * Transformation.scale(1.2, 1.0, 1.2), lightMat, 3, SamplerStrategy.Jittered, true)

    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\cube.obj", Transformation.identity, whiteMat, false, true)
    val sb = AABBTree.construct(s)
    val c1 = new Mesh(sb, Transformation.translation(1.0, 0.825, -1.0) * Transformation.rotate("y", 15.0) * Transformation.scale(0.8, 0.8, 0.8), whiteMat, true)
    val c2 = new Mesh(sb, Transformation.translation(-1.1, 1.65, 1.1) * Transformation.rotate("y", 75.0) * Transformation.scale(0.8, 1.65, 0.8), whiteMat, true)

    val bb = new SimpleList(List(floor, ceiling, backWall, redWall, greenWall, light, c1, c2))
    val ambientLight = new AmbientLight(Color.white, 0.0)
    new Scene(perspectiveCamera, bb, ambientLight, List(light), Color(0.0, 0.0, 0.0), maxDepthDirect, maxDepthIndirect)
  }

  def cornellBoxReflectiveSphere(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
    val cameraPosition = Transformation.translation(0, 2.73, -7.2) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0), 60)

    val whiteMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color(0.9, 0.8, 0.6)),
      true)

    val greenMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color(0.0, 0.8, 0.0)),
      true)

    val redMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color(0.8, 0.0, 0.0)),
      true)

    val refMat = new ReflectiveMaterial(PerfectSpecularBRDF(1.0, Color(0.9, 0.9, 0.9)), true)

    val lightMat = new EmissiveMaterial(1.0, Color(1.0, 0.8, 0.6), true)

    val floor = new FinitePlane(Transformation.scale(5.56, 1.0, 5.56), whiteMat, true)
    val ceiling = new FinitePlane(Transformation.translation(0.0, 5.488, 0.0) * Transformation.rotate("x", 180.0) * Transformation.scale(5.56, 1.0, 5.56), whiteMat, true)
    val backWall = new FinitePlane(Transformation.translation(0.0, 2.744, 2.78) * Transformation.rotate("x", -90.0) * Transformation.scale(5.56, 1.0, 5.488), whiteMat, true)
    val redWall = new FinitePlane(Transformation.translation(2.78, 2.744, 0.0) * Transformation.rotate("z", 90.0) * Transformation.scale(5.488, 1.0, 5.56), redMat, true)
    val greenWall = new FinitePlane(Transformation.translation(-2.78, 2.744, 0.0) * Transformation.rotate("z", -90.0) * Transformation.scale(5.488, 1.0, 5.56), greenMat, true)
    val light = new PlaneAreaLight(Transformation.translation(0.0, 5.48799, 0.0) * Transformation.rotate("x", 180) * Transformation.scale(1.2, 1.0, 1.2), lightMat, 3, SamplerStrategy.Jittered, true)

    val sphere = new Sphere(Transformation.translation(1.0, 0.5, -1.0) * Transformation.rotate("y", 15.0) * Transformation.scale(1.0, 1.0, 1.0), refMat, true)
    //val tallBox = new Cube(Transformation.translation(-1.1, 1.65, 1.1) * Transformation.rotate("y", 75.0) * Transformation.scale(1.65, 3.3, 1.65), whiteMat, true)
    //val tallCylinder = new Cylinder(Transformation.translation(-1.1, 1.65, 1.1) * Transformation.rotate("y", 75.0) * Transformation.scale(0.8, 1.65, 0.8), whiteMat, true)
    val s: List[FiniteGeometricObject] = ObjParser.parse("..\\obj\\cube.obj", Transformation.identity, whiteMat, false, true)
    val sb = AABBTree.construct(s)
    val c = new Mesh(sb, Transformation.translation(-1.1, 1.65, 1.1) * Transformation.rotate("y", 75.0) * Transformation.scale(0.8, 1.65, 0.8), whiteMat, true)

    val bb = new SimpleList(List(floor, ceiling, backWall, redWall, greenWall, light, sphere, c))//tallCylinder))//, tallBox))
    val ambientLight = new AmbientLight(Color.white, 0.0)
    new Scene(perspectiveCamera, bb, ambientLight, List(light), Color(0.0, 0.0, 0.0), maxDepthDirect, maxDepthIndirect)
  }

  def cornellBoxGlossyTest(width: Int, height: Int, maxDepthDirect: Int, maxDepthIndirect: Int): Scene = {
    val cameraPosition = Transformation.translation(0, 2.73, -7.2) transform Point3d.origin

    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0), 60)

    val whiteMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color(0.9, 0.8, 0.6)),
      true)

    val greenMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color(0.0, 0.8, 0.0)),
      true)

    val redMat = new LambertMaterial(LambertianBRDF(0.0, Color.white),
      LambertianBRDF(1.0, Color(0.8, 0.0, 0.0)),
      true)

    val glossyMat = new GlossyMaterial(SpecularBRDF(1.0, Color(0.8, 0.7, 0.5), 10000.0), true)

    val lightMat = new EmissiveMaterial(0.9, Color(1.0, 0.8, 0.6), true)

    val floor = new FinitePlane(Transformation.scale(5.56, 1.0, 5.56), whiteMat, true)
    val ceiling = new FinitePlane(Transformation.translation(0.0, 5.488, 0.0) * Transformation.rotate("x", 180.0) * Transformation.scale(5.56, 1.0, 5.56), whiteMat, true)
    val backWall = new FinitePlane(Transformation.translation(0.0, 2.744, 2.78) * Transformation.rotate("x", -90.0) * Transformation.scale(5.56, 1.0, 5.488), whiteMat, true)
    val redWall = new FinitePlane(Transformation.translation(2.78, 2.744, 0.0) * Transformation.rotate("z", 90.0) * Transformation.scale(5.488, 1.0, 5.56), redMat, true)
    val greenWall = new FinitePlane(Transformation.translation(-2.78, 2.744, 0.0) * Transformation.rotate("z", -90.0) * Transformation.scale(5.488, 1.0, 5.56), greenMat, true)
    val light = new PlaneAreaLight(Transformation.translation(0.0, 5.48799, 0.0) * Transformation.rotate("x", 180) * Transformation.scale(1.2, 1.0, 1.2), lightMat, 3, SamplerStrategy.Jittered, true)

    val sphere = new Sphere(Transformation.translation(0.0, 1.2, 0.0) * Transformation.scale(1.0, 1.0, 1.0), glossyMat, true)

    val bb = new SimpleList(List(floor, ceiling, backWall, redWall, greenWall, light, sphere))
    val ambientLight = new AmbientLight(Color.white, 0.0)
    new Scene(perspectiveCamera, bb, ambientLight, List(light), Color(0.0, 0.0, 0.0), maxDepthDirect, maxDepthIndirect)
  }
}
