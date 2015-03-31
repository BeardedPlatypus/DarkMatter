package com.beardedplatypus.world.geometry.objects.polygon

import com.beardedplatypus.math.{Point3d, Point2d, Transformation, Vector3d}
import com.beardedplatypus.shading.material.Material

import scala.collection.mutable.ArrayBuffer
import scala.util.parsing.combinator._

private class ObjParser extends RegexParsers {
  // Parser related functions.
  // -------------------------------------------------------------------------------------------------------------------
  def coord: Parser[Double] = """(-?\d+\.\d*)""".r ^^ { _.toDouble }
  def index: Parser[Int] = """(0|[1-9]\d*)""".r ^^ { _.toInt }
  // -------------------------------------------------------------------------------------------------------------------
  def vector: Parser[Vector3d] = coord ~ coord ~ coord ^^ { case x ~ y ~ z => new Vector3d(x, y, z) }
  def point: Parser[Point3d] = coord ~ coord ~ coord ^^ { case x ~ y ~ z => new Point3d(x, y, z) }
  def pointUV: Parser[Point2d] = coord ~ coord ^^ { case u ~ v => new Point2d(u, v) }

  def vertex: Parser[(Int, Int, Int)] = index~"/"~index~"/"~index ^^ {case i1~s1~i2~s2~i3 => (i1 - 1, i2 - 1, i3  - 1)}
  // -------------------------------------------------------------------------------------------------------------------
  def v: Parser[Unit] = "v"~point ^^ {
    case id~p => {
      this.vertices.append(p)
      this.normalsVertices.append(Vector3d.zero)
    }
  }
  def vt: Parser[Unit] = "vt"~pointUV ^^ { case id~p => this.coordsUV.append(p) }
  def vn: Parser[Unit] = "vn"~vector ^^ { case id~v => this.normals.append(v) }
  def tri: Parser[Unit] = "f" ~ vertex ~ vertex ~ vertex ^^ {
    case id ~ v1 ~ v2 ~ v3 => { //FIXME: find out how to unpack this at the beginning such that it is more readal
      normalsVertices(v1._1) += normals(v1._3)
      normalsVertices(v2._1) += normals(v2._3)
      normalsVertices(v3._1) += normals(v3._3)
      faces.append(((v1._1, v2._1, v3._1), (v1._2, v2._2, v3._2), (normals(v1._3) + normals(v2._3) + normals(v3._3)).normalized))
    }
  }

  def line: Parser[Unit] = v | vt | vn | tri

  // -------------------------------------------------------------------------------------------------------------------
  // Methods related constructing
  val vertices: ArrayBuffer[Point3d] = new ArrayBuffer()
  val normalsVertices: ArrayBuffer[Vector3d] = new ArrayBuffer()
  val normals: ArrayBuffer[Vector3d] = new ArrayBuffer()
  val coordsUV: ArrayBuffer[Point2d] = new ArrayBuffer()
  val faces: ArrayBuffer[((Int, Int, Int), (Int, Int, Int), Vector3d)] = new ArrayBuffer()
}

object ObjParser {
  def parse(filePath: String, t: Transformation, mat: Material, shadow: Boolean, isFlat: Boolean): List[MeshTriangle] = {
    print("construct parser ... ")
    val parser = new ObjParser()
    println("done")

    print("starting reading file ... ")
    for (line <- io.Source.fromFile(filePath).getLines) parser.parse(parser.line, line)
    println("done")

    print("construct vertices ... ")
    val vertices = for (i <- 0 to parser.vertices.length - 1) yield new Vertex(parser.normalsVertices(i).normalized, parser.vertices(i))
    println("done")

    (if (isFlat) for (f <- parser.faces) yield  f match { case ((v1, v2, v3), (t1, t2, t3), n) =>  MeshTriangle.flat(vertices(v1), vertices(v2), vertices(v3),
                                                                                                              parser.coordsUV(t1), parser.coordsUV(t2), parser.coordsUV(t3),
                                                                                                              n, t, mat, shadow) }
    else for (f <- parser.faces) yield  f match { case ((v1, v2, v3), (t1, t2, t3), n) =>  MeshTriangle.smooth(vertices(v1), vertices(v2), vertices(v3),
                                                                                                                        parser.coordsUV(t1), parser.coordsUV(t2), parser.coordsUV(t3),
                                                                                                                        n, t, mat, shadow) }).toList
  }
}

//object ObjBuilder {
//
//
//  def parse(filePath: String, t: Transformation, mat: Material, shadow: Boolean, isFlat: Boolean): List[MeshTriangle] = {
//    val vertex = """v (-?\d+\.\d*) (-?\d+\.\d*) (-?\d+\.\d*)""".r
//    val vertexTexture = """vt (-?\d+\.\d*) (-?\d+\.\d*)""".r
//    val vertexNormal = """vn (-?\d+\.\d*) (-?\d+\.\d*) (-?\d+\.\d*)""".r
//    val face = """f (\d+)/(\d+)/(\d+) (\d+)/(\d+)/(\d+) (\d+)/(\d+)/(\d+)""".r
//
//
//    @tailrec
//    def readRecursive(lines: List[String],
//                      accV: List[Point3d],
//                      accVn: List[Vector3d],
//                      accF: List[((Int, Int, Int),
//                                  (Int, Int, Int),
//                                  (Int, Int, Int))]): (Vector[Point3d],
//                                                       Vector[Vector3d],
//                                                       List[((Int, Int, Int),
//                                                             (Int, Int, Int),
//                                                             (Int, Int, Int))])  = {
//      if (lines.isEmpty) (accV.toVector, accVn.toVector, accF)
//      else {
//        lines.head match {
//          case vertex(x, y, z) => {
//            readRecursive(lines.tail,
//              new Point3d(x.toDouble, y.toDouble, z.toDouble) :: accV,
//              accVn, accF)
//          }
//          case vertexNormal(x, y, z) => readRecursive(lines.tail, accV,
//                                                      Vector3d(x.toDouble, y.toDouble, z.toDouble) :: accVn,
//                                                      accF)
//          case face(v1, vt1, vn1,
//                    v2, vt2, vn2,
//                    v3, vt3, vn3) => readRecursive(lines.tail, accV, accVn,
//                                                   ((v1.toInt -1, vt1.toInt -1, vn1.toInt -1),
//                                                    (v2.toInt -1, vt2.toInt -1, vn2.toInt -1),
//                                                    (v3.toInt -1, vt3.toInt -1, vn3.toInt -1)) :: accF)
//          case _ => readRecursive(lines.tail, accV, accVn, accF)
//        }
//      }
//    }
//
//    print("starting reading file ...")
//    val fileLines: List[String] = io.Source.fromFile(filePath).getLines.toList.reverse
//    println("done")
//
//    print("starting parsing...")
//    val (vertices, vertexNormals: Vector[Vector3d], faces) = readRecursive(fileLines, Nil, Nil, Nil)
//    println("done")
//
////    print("starting vertexTriples...")
////    val vertexTriples: List[(Int, Int, Int)] = faces.flatMap({case (a: (Int, Int, Int), b: (Int, Int, Int), c: (Int, Int, Int)) => a :: b :: c :: Nil})
////    println("done")
////
////    print("starting vertexNormalMap...")
////    val array: Array[Vector3d] = new Array[Vector3d](vertices.length)
////    for (i <- 0 to vertices.length - 1) array(i) = Vector3d.zero
////
////    for ((vertex, texture, normal) <- vertexTriples) array(vertex) += vertexNormals(normal)
////    val verticesArray = for (i <- 0 to vertices.length - 1) yield new Vertex(array(i).normalized, vertices(i))
//
//
//    // FIXME ask if this was smart to do. probably not
//    print("starting vertexTriples...")
//    val vertexTriples: List[(Int, Int, Int)] = faces.flatMap({case (a: (Int, Int, Int), b: (Int, Int, Int), c: (Int, Int, Int)) => a :: b :: c :: Nil})
//    println("done")
//
//    print("starting vertexNormalMap...")
//    val array: Array[Vector3d] = new Array[Vector3d](vertices.length)
//    for (i <- 0 to vertices.length - 1) array(i) = Vector3d.zero
//
//    for ((vertex, texture, normal) <- vertexTriples) array(vertex) += vertexNormals(normal)
//    val verticesArray = for (i <- 0 to vertices.length - 1) yield new Vertex(array(i).normalized, vertices(i))
////
////    //val vertexNormalMap: Map[Int, List[Vector3d]] = (vertexTriples map {case (a: Int, b: Int, c: Int) => (a, c)}) groupBy (_._1) mapValues ((l: List[(Int, Int)]) => l map {case (a: Int, b: Int) => vertexNormals(b)})
////    println("done")
////
////    // REFACTOR THIS if it works
////    def calcVertex(k: Int, l: List[Vector3d]): Vertex = {
////      @tailrec
////      def calcNorm(l: List[Vector3d], acc: Vector3d): Vector3d = l match {
////        case Nil => acc
////        case x :: xs => calcNorm(xs, acc + x)
////      }
////      new Vertex(calcNorm(l, Vector3d.zero).normalized, vertices(k))
////    }
////
////    print("calculating vertices...")
////    val verticesList: Map[Int, Vertex] = vertexNormalMap map {case ((key: Int, value: List[Vector3d])) => (key, calcVertex(key, value))}
////    println("done")
//
//    print("constructing triangles...")
//    faces map { case ((v1: Int, vt1: Int, vn1: Int), (v2: Int, vt2: Int, vn2: Int), (v3: Int, vt3: Int, vn3: Int)) =>
//        MeshTriangle(verticesArray(v1), verticesArray(v2), verticesArray(v3),
//                                  (vertexNormals(vn1) + vertexNormals(vn2) + vertexNormals(vn3)).normalized,
//                                  t, mat, shadow, isFlat)
//    }
//  }
//}
