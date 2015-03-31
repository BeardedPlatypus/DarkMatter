package com.beardedplatypus.main

import java.io.File

import com.beardedplatypus.camera.PerspectiveCamera
import com.beardedplatypus.math.{Vector3d, Point3d, Transformation}
import com.beardedplatypus.shading.{Texture, Color}
import com.beardedplatypus.shading.brdf.{GlossySpecularBRDF, LambertianBRDF}
import com.beardedplatypus.shading.material.{NormalMaterial, LambertMaterial, Material}
import com.beardedplatypus.world.geometry.acceleration_structures.{SimpleList, AABBTree}
import com.beardedplatypus.world.geometry.objects.polygon._
import com.beardedplatypus.world.geometry.GeometricObject
import world.geometry.objects.primitives.{BoundingBoxCube, Cube, Cylinder, Sphere}

//,FlatMeshTriangle}
import com.beardedplatypus.world.{Scene, AmbientLight, Light, PointLight}
import com.beardedplatypus.world.geometry._
import shading.material.PhongMaterial

/**
 * Created by Month on 24/02/2015.
 */
object SceneBuilder {
  //  def scene01(width: Int, height: Int): Scene = {
  //    // basicSphereMatteSinglePointLightNoShadow
  //    // Camera
  //    lazy val cameraPosition = Transformation.translation(0.0, 0.0, -4.0) transform Point3d.origin
  //
  //    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
  //      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
  //      90)
  //
  //    // Shapes
  //    //val t1 = Transformation.identity
  //    val t1 = Transformation.identity//scale(1.0, 2.0, 1.0)
  //    val matte: Material = new NormalMaterial()
  //
  //
  //    val triangle: FlatMeshTriangle = new FlatMeshTriangle(new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //                                                                     new Point3d(1.0, 0.0, 0.0)),
  //                                                          new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //                                                                     new Point3d(-1.0, 0.0, 0.0)),
  //                                                          new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //                                                                     new Point3d(0.0, 2.0, 0.0)),
  //                                                          new Vector3d(0.0, 0.0, -1.0),
  //                                                          t1, matte, false)
  //    //        val s1 = new Cube(Transformation.translation(2.0, 0.0, 0.0), matte, false)
  //    //        val s3 = new Cube(Transformation.translation(2.0, 1.0, 0.0), matte, false)
  //    //        val s2 = new Cylinder(t1, matte, false)
  //    //        val s4 = new Cylinder(Transformation.translation(-2.0, 0.0, 0.0), matte, false)
  //    //
  //    //        val s5 = new Cube(Transformation.translation(-3.0, -2.0, 0.0), matte, false)
  //    //
  //    //
  //    //        val crr = new Cube(Transformation.translation(0.5, -1.5, 0.5), matte2, false)
  //    //        val clr = new Cube(Transformation.translation(0.5, -1.5, -0.5), matte3, false)
  //    //        val crl = new Cube(Transformation.translation(-0.5, -1.5, 0.5), matte3, false)
  //    //        val cll = new Cube(Transformation.translation(-0.5, -1.5, -0.5), matte2, false)
  //
  //    //        println("crr: " + crr.boundingBox.toString)
  //    //        println("clr: " + clr.boundingBox.toString)
  //    //        println("crl: " + crl.boundingBox.toString)
  //    //        println("cll: " + cll.boundingBox.toString)
  //    //
  //    //        println("s4: " + s4.boundingBox.toString)
  //    //
  //    //        val shapes: List[GeometricObject] = List(s1, s2, s3, s4, s5, crr, clr, crl, cll)
  //
  //    // lights
  //    val pl1 = new Point3d(0.0, 10.0, -5.0)
  //    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
  //    val lights: List[Light] = List(l1)
  //
  //    val ambientLight = new AmbientLight(Color.white, 0.5)
  //
  //    new Scene(perspectiveCamera,
  //      new AABBTree(triangle.boundingBox),
  //      ambientLight,
  //      lights,
  //      Color.gray)
  //  }
  //
  //  def scene00(width: Int, height: Int): Scene = {
  //    // basicSphereMatteSinglePointLightNoShadow
  //    // Camera
  //    lazy val cameraPosition = Transformation.translation(0.0, 2.0, -4.0) transform Point3d.origin
  //
  //    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
  //      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
  //      90)
  //
  //    // Shapes
  //    //val t1 = Transformation.identity
  //    val t1 = Transformation.identity//scale(1.0, 2.0, 1.0)
  //    val matte: Material = new NormalMaterial()
  //
  //
  //    val bbCube = new BoundingBoxCube((new Point3d(-1, -1, -1), new Point3d(1,1,1)),t1, matte, false)
  //    //        val s1 = new Cube(Transformation.translation(2.0, 0.0, 0.0), matte, false)
  //    //        val s3 = new Cube(Transformation.translation(2.0, 1.0, 0.0), matte, false)
  //    //        val s2 = new Cylinder(t1, matte, false)
  //    //        val s4 = new Cylinder(Transformation.translation(-2.0, 0.0, 0.0), matte, false)
  //    //
  //    //        val s5 = new Cube(Transformation.translation(-3.0, -2.0, 0.0), matte, false)
  //    //
  //    //
  //    //        val crr = new Cube(Transformation.translation(0.5, -1.5, 0.5), matte2, false)
  //    //        val clr = new Cube(Transformation.translation(0.5, -1.5, -0.5), matte3, false)
  //    //        val crl = new Cube(Transformation.translation(-0.5, -1.5, 0.5), matte3, false)
  //    //        val cll = new Cube(Transformation.translation(-0.5, -1.5, -0.5), matte2, false)
  //
  //    //        println("crr: " + crr.boundingBox.toString)
  //    //        println("clr: " + clr.boundingBox.toString)
  //    //        println("crl: " + crl.boundingBox.toString)
  //    //        println("cll: " + cll.boundingBox.toString)
  //    //
  //    //        println("s4: " + s4.boundingBox.toString)
  //    //
  //    //        val shapes: List[GeometricObject] = List(s1, s2, s3, s4, s5, crr, clr, crl, cll)
  //
  //    // lights
  //    val pl1 = new Point3d(0.0, 10.0, -5.0)
  //    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
  //    val lights: List[Light] = List(l1)
  //
  //    val ambientLight = new AmbientLight(Color.white, 0.5)
  //
  //    new Scene(perspectiveCamera,
  //      new AABBTree(bbCube.boundingBox),
  //      ambientLight,
  //      lights,
  //      Color.gray)
  //  }
  //
  //
  //  def scene03(width: Int, height: Int): Scene = {
  //    // basicSphereMatteSinglePointLightNoShadow
  //    // Camera
  //    lazy val cameraPosition = Transformation.translation(0.0, 0.0, -4.0) transform Point3d.origin
  //
  //    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
  //      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
  //      90)
  //
  //    // Shapes
  //    //val t1 = Transformation.identity
  //    val t1 = Transformation.identity//scale(1.0, 2.0, 1.0)
  //    val matte: Material = new NormalMaterial()
  //
  //
  //    val triangle: FlatMeshTriangle = new FlatMeshTriangle(new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //      new Point3d(-1.0, 0.0, 0.0)),
  //      new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //        new Point3d(-3.0, 0.0, 0.0)),
  //      new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //        new Point3d(-2.0, 2.0, 0.0)),
  //      new Vector3d(0.0, 0.0, -1.0),
  //        t1, matte, false)
  //
  //    val triangle2: FlatMeshTriangle = new FlatMeshTriangle(new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //      new Point3d(3.0, 0.0, 0.0)),
  //      new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //        new Point3d(1.0, 0.0, 0.0)),
  //      new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //        new Point3d(2.0, 2.0, 0.0)),
  //      new Vector3d(0.0, 0.0, -1.0),
  //      t1, matte, false)
  //
  //    val triangle3: FlatMeshTriangle = new FlatMeshTriangle(new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //      new Point3d(-1.0, -3.0, 0.0)),
  //      new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //        new Point3d(-3.0, -3.0, 0.0)),
  //      new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //        new Point3d(-2.0, -1.0, 0.0)),
  //      new Vector3d(0.0, 0.0, -1.0),
  //      t1, matte, false)
  //
  //    val triangle4: FlatMeshTriangle = new FlatMeshTriangle(new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //      new Point3d(3.0, -3.0, 0.0)),
  //      new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //        new Point3d(1.0, -3.0, 0.0)),
  //      new Vertex(new Vector3d(0.0, 0.0, -1.0),
  //        new Point3d(2.0, -1.0, 0.0)),
  //      new Vector3d(0.0, 0.0, -1.0),
  //      t1, matte, false)
  //
  //    //println(triangle.boundingBox)
  //
  //    // lights
  //    val pl1 = new Point3d(0.0, 10.0, -5.0)
  //    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
  //    val lights: List[Light] = List(l1)
  //
  //    val ambientLight = new AmbientLight(Color.white, 0.5)
  //
  //    val bb = AABBTree.construct(List(triangle, triangle2))//, triangle3, triangle4)),
  //    println(bb)
  //
  //    new Scene(perspectiveCamera,
  //      bb,
  //      ambientLight,
  //      lights,
  //      Color.gray)
  //  }

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
    lazy val cameraPosition = Transformation.translation(0.0, 0.5, -1.5) transform Point3d.origin

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
}

//  def scene06(width: Int, height: Int): Scene = {
//    // basicSphereMatteSinglePointLightNoShadow
//    // Camera
//    lazy val cameraPosition = Transformation.translation(0.0, 2.0, -4.0) transform Point3d.origin
//
//    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
//      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
//      90)
//
//    // Shapes
//    //val t1 = Transformation.identity
//    val t1 = Transformation.identity//scale(1.0, 2.0, 1.0)
//    val matte: Material = new LambertMaterial(new LambertianBRDF(0.08, Color.white),
//        new LambertianBRDF(0.92, Color.white),
//        true)
//    val t0: Double = System.nanoTime()
//    val s: List[FiniteGeometricObject] = ObjBuilder.parse("src\\resources\\bunny.obj", t1, matte, false, false)
//    val time1: Double = System.nanoTime()
//    println("Total time building triangles: " + ((time1 - t0) / 1000000000.0))
//
//    val t2 = System.nanoTime()
//    val bb = AABBTree.construct(s)
//    val t3 = System.nanoTime()
//    println("Total time building bounding box: " + ((t3 - t2) / 1000000000.0))
//
//    // lights
//    val pl1 = new Point3d(0.0, 10.0, -5.0)
//    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
//    val lights: List[Light] = List(l1)
//
//    val ambientLight = new AmbientLight(Color.white, 0.5)
//
//    println("here: " + s.length)
//    println(bb)
//
//    new Scene(perspectiveCamera,
//      bb,
//      ambientLight,
//      lights,
//      Color.gray)
//  }
//
//  def scene08(width: Int, height: Int): Scene = {
//    // basicSphereMatteSinglePointLightNoShadow
//    // Camera
//    lazy val cameraPosition = Transformation.translation(0.0, 5.0, -10.0) transform Point3d.origin
//
//    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
//      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
//      60)
//
//    // Shapes
//    //val t1 = Transformation.identity
//    val t1 = Transformation.identity//scale(1.0, 2.0, 1.0)
//    val matte: Material = new LambertMaterial(new LambertianBRDF(0.08, Color.white),
//        new LambertianBRDF(0.92, Color.white),
//        true)
//    val t0: Double = System.nanoTime()
//    val s: List[FiniteGeometricObject] = ObjBuilder.parse("src\\resources\\teapot.obj", t1, matte, false, false)
//    val time1: Double = System.nanoTime()
//    println("Total time building triangles: " + ((time1 - t0) / 1000000000.0))
//
//    val t2 = System.nanoTime()
//    val bb = AABBTree.construct(s)
//    val t3 = System.nanoTime()
//    println("Total time building bounding box: " + ((t3 - t2) / 1000000000.0))
//
//    // lights
//    val pl1 = new Point3d(0.0, 10.0, -5.0)
//    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
//    val lights: List[Light] = List(l1)
//
//    val ambientLight = new AmbientLight(Color.white, 0.5)
//
//    println("here: " + s.length)
//    println(bb)
//
//    new Scene(perspectiveCamera,
//      bb,
//      ambientLight,
//      lights,
//      Color.gray)
//  }
//
//  def scene07(width: Int, height: Int): Scene = {
//    // basicSphereMatteSinglePointLightNoShadow
//    // Camera
//    lazy val cameraPosition = Transformation.translation(0.0, 2.0, 3.5) transform Point3d.origin
//
//    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
//      Vector3d(0.0, 0.0, -1.0), Vector3d(0.0, 1.0, 0.0),
//      90)
//
//    // Shapes
//    //val t1 = Transformation.identity
//    val t1 = Transformation.identity//scale(1.0, 2.0, 1.0)
//    val matte: Material = new LambertMaterial(new LambertianBRDF(0.08, Color.white),
//        new LambertianBRDF(0.92, Color.white),
//        true)
//    val t0: Double = System.nanoTime()
//    val s: List[FiniteGeometricObject] = ObjBuilder.parse("src\\resources\\castle_crasher.obj", t1, matte, false, false)
//    val time1: Double = System.nanoTime()
//    println("Total time building triangles: " + ((time1 - t0) / 1000000000.0))
//
//    val t2 = System.nanoTime()
//    val bb = AABBTree.construct(s)
//    val t3 = System.nanoTime()
//    println("Total time building bounding box: " + ((t3 - t2) / 1000000000.0))
//
//    // lights
//    val pl1 = new Point3d(0.0, 10.0, 5.0)
//    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
//    val lights: List[Light] = List(l1)
//
//    val ambientLight = new AmbientLight(Color.white, 0.5)
//
//    println("here: " + s.length)
//    println(bb)
//
//    new Scene(perspectiveCamera,
//      bb,
//      ambientLight,
//      lights,
//      Color.gray)
//  }
//
//  //    def scene01(width: Int, height: Int): Scene = {
////      // basicSphereMatteSinglePointLightNoShadow
////      // Camera
////      lazy val cameraPosition = Transformation.translation(0.0, 0.0, -4.0) transform Point3d.origin
////
////      val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
////        Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
////        90)
////
////      // Shapes
////      //val t1 = Transformation.identity
////      val t1 = Transformation.scale(1.0, 2.0, 1.0)
////      val matte: Material = new LambertMaterial(new LambertianBRDF(0.0, Color.white),
////                                                new LambertianBRDF(1.0, Color.green),
////                                                false)
////
////      val matte2: Material = new LambertMaterial(new LambertianBRDF(0.0, Color.white),
////        new LambertianBRDF(1.0, Color.red),
////        false)
////      val matte3: Material = new LambertMaterial(new LambertianBRDF(0.0, Color.white),
////        new LambertianBRDF(1.0, Color.blue),
////        false)
////
////
////      val s1 = new Cube(Transformation.translation(2.0, 0.0, 0.0), matte, false)
////      val s3 = new Cube(Transformation.translation(2.0, 1.0, 0.0), matte, false)
////      val s2 = new Cylinder(t1, matte, false)
////      val s4 = new Cylinder(Transformation.translation(-2.0, 0.0, 0.0), matte, false)
////
////      val s5 = new Cube(Transformation.translation(-3.0, -2.0, 0.0), matte, false)
////
////
////      val crr = new Cube(Transformation.translation(0.5, -1.5, 0.5), matte2, false)
////      val clr = new Cube(Transformation.translation(0.5, -1.5, -0.5), matte3, false)
////      val crl = new Cube(Transformation.translation(-0.5, -1.5, 0.5), matte3, false)
////      val cll = new Cube(Transformation.translation(-0.5, -1.5, -0.5), matte2, false)
////
////      println("crr: " + crr.boundingBox.toString)
////      println("clr: " + clr.boundingBox.toString)
////      println("crl: " + crl.boundingBox.toString)
////      println("cll: " + cll.boundingBox.toString)
////
////      println("s4: " + s4.boundingBox.toString)
////
////      val shapes: List[GeometricObject] = List(s1, s2, s3, s4, s5, crr, clr, crl, cll)
////
////      // lights
////      val pl1 = new Point3d(0.0, 10.0, -5.0)
////      val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
////      val lights: List[Light] = List(l1)
////
////      val ambientLight = new AmbientLight(Color.white, 0.5)
////
////      new Scene(perspectiveCamera,
////        shapes,
////        ambientLight,
////        lights,
////        Color.gray)
////    }
////
////  def scene02(width: Int, height: Int): Scene = {
////    // basicSphereMatteSinglePointLightNoShadow
////    // Camera
////    lazy val cameraPosition = Transformation.translation(0.0, 0.0, -4.0) transform Point3d.origin
////
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, cameraPosition,
////      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
////      90)
////
////    // Shapes
////    val t1 = Transformation.identity
////    //val t1 = Transformation.scale(2.0, 2.0, 2.0)
////    val matte: Material = new LambertMaterial(new LambertianBRDF(0.0, Color.white),
////      new LambertianBRDF(1.0, Color.green),
////      false)
////
////    val s1 = new Cube(t1, matte, false)
////    val shapes: List[GeometricObject] = List(s1)
////
////    println("s1: " + s1.boundingBox.toString)
////    // lights
////    val pl1 = new Point3d(0.0, 10.0, -10.0)
////    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
////    val lights: List[Light] = List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.5)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
//
//
//  //  def scene01(width: Int, height: Int): Scene = {
////    // basicSphereMatteSinglePointLightNoShadow
////    // Camera
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, Point3d.origin,
////      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
////      90)
////
////    // Shapes
////    val t1 = Transformation.translation(0.0, 0.0, 10.0)
////    val matte: Material = new LambertMaterial(new LambertianBRDF(0.0, Color.white),
////                                              new LambertianBRDF(1.0, Color.green),
////                                              false)
////
////    val s1 = new Sphere(t1, 5.0, matte, false)
////    val shapes: List[GeometricObject] = List(s1)
////
////    // lights
////    val pl1 = new Point3d(0.0, 20.0, 10.0)
////    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
////    val lights: List[Light] = List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.5)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene02(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightnoShadow
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, Point3d.origin,
////    Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
////    90)
////
////    val t1 = Transformation.translation(0.0, 0.0, 10.0)
////    val t2 = Transformation.translation(0.0, -5.0, 0.0)
////
////    val matte: Material = new LambertMaterial(new LambertianBRDF(0.0, Color.white),
////    new LambertianBRDF(1.0, Color.green),
////    false)
////
////    val s1 = new Sphere(t1, 5.0, matte, false)
////    val plane = new Plane(t2, Vector3d(0.0, 1.0, 0.0), matte, true)
////    val shapes: List[GeometricObject] = List(s1, plane)
////
////    // lights
////    val pl1 = new Point3d(0.0, 20.0, 10.0)
////    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, false)
////    val lights: List[Light] = List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.5)
////
////    new Scene(perspectiveCamera,
////    shapes,
////    ambientLight,
////    lights,
////    Color.gray)
////  }
////
////  def scene03(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, Point3d.origin,
////      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
////      90)
////
////    val t1 = Transformation.translation(0.0, 0.0, 10.0)
////    val t2 = Transformation.translation(0.0, -5.0, 0.0)
////
////    val matte: Material = new LambertMaterial(new LambertianBRDF(0.1, Color.white),
////      new LambertianBRDF(1.0, Color.green),
////      true)
////
////    val matte2: Material = new LambertMaterial(new LambertianBRDF(0.0, Color.white),
////      new LambertianBRDF(1.0, Color.white),
////      true)
////
////
////    val s1 = new Sphere(t1, 5.0, matte, true)
////    val plane = new Plane(t2, Vector3d(0.0, 1.0, 0.0), matte2, true)
////    val shapes: List[GeometricObject] = List(s1, plane)
////
////    // lights
////    val pl1 = new Point3d(1.0, 10.0, 0.0)
////    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val lights: List[Light] = List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.5)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene04(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, Point3d.origin,
////      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
////      90)
////
////    val t1 = Transformation.translation(0.0, 0.0, 10.0)
////    val t2 = Transformation.translation(0.0, -2.5, 0.0)
////    val t3 = Transformation.translation(-2.5, -1.5, 6)
////    val t4 = Transformation.translation(2.5, -1.5, 6)
////
////    val matte: Material = new LambertMaterial(new LambertianBRDF(0.05, Color.white),
////      new LambertianBRDF(1.0, Color.green),
////      true)
////
////    val matte2: Material = new LambertMaterial(new LambertianBRDF(0.0, Color.white),
////      new LambertianBRDF(0.3, Color.white),
////      true)
////
////    val matte3: Material = new LambertMaterial(new LambertianBRDF(0.05, Color.white),
////      new LambertianBRDF(1.0, Color.blue),
////      true)
////    val matte4: Material = new LambertMaterial(new LambertianBRDF(0.05, Color.white),
////      new LambertianBRDF(1.0, Color.red),
////      true)
////
////
////    val s1 = new Sphere(t1, 3.0, matte, true)
////    val s2 = new Sphere(t3, 1, matte3, true)
////    val s3 = new Sphere(t4, 1, matte4, true)
////    val plane = new Plane(t2, Vector3d(0.0, 1.0, 0.0), matte2, true)
////    val shapes: List[GeometricObject] = List(s1, s2, s3, plane)
////
////    // lights
////    val pl1 = new Point3d(1.0, 10.0, 0.0)
////    val l1 = new PointLight(Color.white, 1.0, pl1, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val pl2 = new Point3d(-1.0, 10.0, 0.0)
////    val l2 = new PointLight(Color.white, 1.0, pl2, (p1: Point3d, p2: Point3d) => 1.0, false)
////
////
////    val lights: List[Light] = List(l1, l2)
////
////
////    val ambientLight = new AmbientLight(Color.white, 0.05)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene05(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, Point3d.origin,
////      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
////      90)
////
////    val t1 = Transformation.translation(0.0, 0.0, 10.0)
////    val t2 = Transformation.translation(0.0, -5.0, 0.0)
////
////    val matte: Material = new PhongMaterial(new LambertianBRDF(0.2, Color.white),
////                                            new LambertianBRDF(0.5, Color.blue),
////                                            new GlossySpecularBRDF(0.4, Color.white, 5),
////                                            true)
////
////    val matte2: Material = new LambertMaterial(new LambertianBRDF(0.6, Color.white),
////      new LambertianBRDF(.4, Color.white),
////      false)
////
////
////    val s1 = new Sphere(t1, 5.0, matte, true)
////    val plane = new Plane(t2, Vector3d(0.0, 1.0, 0.0), matte2, true)
////    val shapes: List[GeometricObject] = List(s1, plane)
////
////    // lights
////    val pl1 = new Point3d(1.0, 10.0, 0.0)
////    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val lights: List[Light] = List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene06(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val rot30: Transformation = Transformation.rotate("x", 30)
////    val lookAt: Vector3d = rot30 transform Vector3d(0.0, 0.0, 1.0)
////    val up: Vector3d = rot30 transform Vector3d(0.0, 1.0, 0.0)
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, new Point3d(0.0, 3.3, 0.0),
////      lookAt, up, 75)
////
////    val dist = 5.0 * Math.tan(Math.toRadians(30)) + 1.2
////    val t1 = Transformation.translation(0.0, 2.5, dist)
////    val t2 = Transformation.translation(0.0, 0.0, 0.0)
////
////    val matte: Material = new PhongMaterial(new LambertianBRDF(0.05, Color.white),
////      new LambertianBRDF(0.6, Color.white),
////      new GlossySpecularBRDF(0.4, Color.white, 50),
////      true)
////
////    val matte2: Material = new LambertMaterial(new LambertianBRDF(0.2, Color.white),
////      new LambertianBRDF(0.8, Color.white),
////      true)
////
////
////    val s1 = new Sphere(t1, 1.0, matte, true)
////    val plane = new Plane(t2, Vector3d(0.0, 1.0, 0.0), matte2, true)
////    val shapes: List[GeometricObject] = List(s1, plane)
////
////    // lights
////    val pl = new Point3d(0.0, 0.0, 12.0)
////    val tL = Transformation.translation(0.0, 35.0, dist)
////
////    val pl1 = tL.transform(pl)
////    val pl2 = tL.transform(Transformation.rotate("y", 120) transform pl)
////    val pl3 = tL.transform(Transformation.rotate("y", -120) transform pl)
////
////
////    println(pl1)
////    println(pl2)
////    println(pl3)
////
////    val l1 = new PointLight(Color.red, 1.1, pl1, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val l2 = new PointLight(Color.green, 1.1, pl2, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val l3 = new PointLight(Color.blue, 1.1, pl3, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val lights: List[Light] = List(l1, l2, l3)
////
////    val ambientLight = new AmbientLight(Color.white, 0.05)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene07(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, Point3d.origin,
////      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
////      90)
////
////    val t1 = Transformation.translation(0.0, 0.0, 10.0)
////
////    val matte: Material = new NormalMaterial()
////
////    val v1: Vertex = new Vertex(Vector3d(0.0, 0.0, -1.0),
////                                new Point3d(-2.5, 0.0, 0.0))
////
////    val v2: Vertex = new Vertex(Vector3d(0.0, 0.0, -1.0),
////      new Point3d(2.5, 0.0, 0.0))
////
////    val v3: Vertex = new Vertex(Vector3d(0.0, 0.0, -1.0),
////      new Point3d(0, 2.5, 0.0))
////
////
////    val triangle: MeshTriangle = SmoothMeshTriangle(v1, v2, v3, Vector3d(0.0, 0.0, -1.0), t1, matte, false)
////
////    val shapes: List[GeometricObject] = List(triangle)
////    val lights: List[Light] = Nil // List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene08(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val rot30: Transformation = Transformation.rotate("x", 30)
////    val rotz30: Transformation = Transformation.rotate("y", 30)
////    val loc: Transformation = Transformation.translation(0.0, 0.0, -3.0)
////    val lookAt: Vector3d = rotz30 transform (rot30 transform Vector3d(0.0, 0.0, 1.0))
////    val up: Vector3d = rotz30 transform  (rot30 transform Vector3d(0.0, 1.0, 0.0))
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, loc transform (new Point3d(0.0, 3.3, 0.0)),
////      lookAt, up, 75)
////
////    val matte: Material = new NormalMaterial()
////
////
////    val shapes: List[GeometricObject] = ObjBuilder.parse("src\\resources\\cube.obj", Transformation.identity, matte, false, true)
////    val lights: List[Light] = Nil // List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene09(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val rot30: Transformation = Transformation.rotate("x", 30)
////    val lookAt: Vector3d = rot30 transform Vector3d(0.0, 0.0, 1.0)
////    val up: Vector3d = rot30 transform Vector3d(0.0, 1.0, 0.0)
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, new Point3d(0.0, 5.3, 0.0),
////      lookAt, up, 65)
////
////    val matte: Material = new NormalMaterial()
////
////    val t1: Transformation = Transformation.translation(-0.8, 0.0, 4.7) * Transformation.rotate("y", 180)
////    val shapes: List[GeometricObject] = ObjBuilder.parse("src\\resources\\bunny.obj", t1, matte, false, false)
////    val lights: List[Light] = Nil // List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene14(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val rot30: Transformation = Transformation.rotate("x", 30)
////    val lookAt: Vector3d = rot30 transform Vector3d(0.0, 0.0, 1.0)
////    val up: Vector3d = rot30 transform Vector3d(0.0, 1.0, 0.0)
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, new Point3d(0.0, 5.3, 0.0),
////      lookAt, up, 65)
////
////    val matte: Material = new PhongMaterial(new LambertianBRDF(0.05, Color.white),
////      new LambertianBRDF(0.6, Color.white),
////      new GlossySpecularBRDF(0.4, Color.white, 50),
////      true)
////
////    val matte2: Material = new LambertMaterial(new LambertianBRDF(0.2, Color.white),
////      new LambertianBRDF(0.8, Color.white),
////      true)
////
////    val plane = new Plane(Transformation.identity, Vector3d(0.0, 1.0, 0.0), matte2, true)
////
////    val pl = new Point3d(0.0, 0.0, 12.0)
////    val tL = Transformation.translation(0.0, 35.0, 4.7)
////
////    val t1: Transformation = Transformation.translation(-0.8, 0.0, 4.7) * Transformation.rotate("y", 180)
////    val shapes: List[GeometricObject] = plane :: ObjBuilder.parse("src\\resources\\torus.obj", t1, matte, true, false)
////
////    val pl1 = tL.transform(pl)
////    val pl2 = tL.transform(Transformation.rotate("y", 120) transform pl)
////    val pl3 = tL.transform(Transformation.rotate("y", -120) transform pl)
////
////
////    println(pl1)
////    println(pl2)
////    println(pl3)
////
////    val l1 = new PointLight(Color.red, 1.1, pl1, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val l2 = new PointLight(Color.green, 1.1, pl2, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val l3 = new PointLight(Color.blue, 1.1, pl3, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val lights: List[Light] = List(l1, l2, l3)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////
////  def scene10(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val rot30: Transformation = Transformation.rotate("x", 30)
////    val lookAt: Vector3d = rot30 transform Vector3d(0.0, 0.0, 1.0)
////    val up: Vector3d = rot30 transform Vector3d(0.0, 1.0, 0.0)
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, new Point3d(0.0, 5.3, 0.0),
////      lookAt, up, 65)
////
////    val matte: Material = new NormalMaterial()
////
////    val t1 = Transformation.translation(0.0, 0.0, 5.0)
////    val shapes: List[GeometricObject] = List(new Cylinder(t1, matte, false))
////    val lights: List[Light] = Nil // List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene11(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val rot30: Transformation = Transformation.rotate("x", 40)
////    val lookAt: Vector3d = rot30 transform Vector3d(0.0, 0.0, 1.0)
////    val up: Vector3d = rot30 transform Vector3d(0.0, 1.0, 0.0)
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, new Point3d(0.0, 3.0, 0.0),
////      lookAt, up, 65)
////
////    val matte: Material = new NormalMaterial()
////
////    val t1 = Transformation.translation(0.0, -1.0, 6.0) * Transformation.rotate("x", -30)
////    val shapes: List[GeometricObject] = List(new Cone(0.0, 1.0, t1, matte, false))
////    val lights: List[Light] = Nil // List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene12(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, Point3d.origin,
////      Vector3d(0.0, 0.0, 1.0), Vector3d(0.0, 1.0, 0.0),
////      90)
////
////    val matte: Material = new NormalMaterial()
////
////    val t1 = Transformation.translation(0.0, 0.0, 4.0)
////    val t2 = Transformation.translation(0.0, -0.5, 4.0)
////    val s1 = new Sphere(t2, 0.5, matte, false)
////    val shapes: List[GeometricObject] = List(s1, new Cone(0, 3.0, t1, matte, false))
////    val lights: List[Light] = Nil // List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
////
////  def scene13(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val rot30: Transformation = Transformation.rotate("x", 40)
////    val lookAt: Vector3d = rot30 transform Vector3d(0.0, 0.0, 1.0)
////    val up: Vector3d = rot30 transform Vector3d(0.0, 1.0, 0.0)
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, new Point3d(0.0, 3.0, 0.0),
////      lookAt, up, 65)
////
////    val matte: Material = new NormalMaterial()
////
////    //val t1 = Transformation.translation(-2.0, -0.1, 1.0)
////    val t1 = Transformation.translation(1.0, 0.0, 3.0)
////    val p1 = new Vector3d(1.0, 0.0, 0.0)
////
////    println(t1.matrix)
////    println(t1.invMatrix)
////    println(t1.matrix * t1.invMatrix)
////
////    val p2 = t1 transformInv p1
////    println(p1)
////    println(p2)
////    println(t1 transform p2)
////
////    val shapes: List[GeometricObject] = List(new Cylinder(t1, matte, false))
////    val lights: List[Light] = Nil // List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
//
////  def demo1(width: Int, height: Int): Scene = {
////    // basicSpherePlaneMatteSinglePointLightShadow
////    val lookAt: Vector3d = Vector3d(0.0, 0.0, 1.0)
////    val up: Vector3d = Vector3d(0.0, 1.0, 0.0)
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, Point3d.origin,
////      lookAt, up, 60)
////
////    val matte: Material = new LambertMaterial(new LambertianBRDF(0.2, Color.white),
////                                              new LambertianBRDF(0.8, Color.white),
////                                                true)
////
////    val t1: Transformation = Transformation.identity
////    val shapes: List[GeometricObject] = ObjBuilder.parse("src\\resources\\torus.obj", t1, matte, true, false)
////
////    val pl1 = new Point3d(1.0, 1.0, 0.0)
////    val l1 = new PointLight(Color.white, 1, pl1, (p1: Point3d, p2: Point3d) => 1.0, true)
////    val lights: List[Light] = List(l1)
////
////    val ambientLight = new AmbientLight(Color.white, 0.2)
////
////    new Scene(perspectiveCamera,
////      shapes,
////      ambientLight,
////      lights,
////      Color.gray)
////  }
//
//  //  def demo_analyticalObjectsNormal(width: Int, height: Int): Scene = {
////    val rot30: Transformation = Transformation.rotate("x", 40)
////    val lookAt: Vector3d = rot30 transform Vector3d(0.0, 0.0, 1.0)
////    val up: Vector3d = rot30 transform Vector3d(0.0, 1.0, 0.0)
////    val perspectiveCamera: PerspectiveCamera = PerspectiveCamera(width, height, new Point3d(0.0, 3.0, 0.0),
////      lookAt, up, 65)
////
////    val matte: Material = new NormalMaterial()
////
////    val plane = new Plane(Transformation.identity, Vector3d(1.0, 0.0, 0.0), matte, true)
////    val sphere1 = new Sphere()
////  }
//}