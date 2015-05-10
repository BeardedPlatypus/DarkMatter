package main

import com.beardedplatypus.main.{RenderSettings, SceneBuilder}
import com.beardedplatypus.sampling.SamplerStrategy
import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy
import com.beardedplatypus.world.Scene

import scala.util.parsing.combinator.RegexParsers

// Reads basic .ini files to configure renderer
// TODO create a better way to implement this.
private class ConfigParser extends RegexParsers {
  def positiveInteger: Parser[Int] = """(0|[1-9]\d*)""".r ^^ { _.toInt }
  def word: Parser[String] = """([a-zA-Z0-9]+)""".r ^^ (_.toString)
  // -------------------------------------------------------------------------
  def pName: Parser[Unit] = "name=" ~ word ^^ {case id~n => this.name = n}
  def pWidth: Parser[Unit] = "width=" ~ positiveInteger ^^ {case id~w => this.width = w}
  def pHeight: Parser[Unit] = "height=" ~ positiveInteger ^^ {case id~h => this.height = h}
  def pBucketSize: Parser[Unit] = "bucket_size=(" ~ positiveInteger ~ "," ~ positiveInteger ~ ")" ^^ {
    case id~bucketX~s~bucketY~e => bucketSize = (bucketX, bucketY)
  }
  def pSamplesRoot: Parser[Unit] = "samples_root=" ~ positiveInteger ^^ {case id~s => this.samplesRoot = s}
  def pSamplerStrategy: Parser[Unit] = "sampler_strategy=" ~ word ^^ {
    case id~w => samplerStrategy = w match {
      case "random" => SamplerStrategy.Random
      case "jittered" => SamplerStrategy.Jittered
      case "constant" => SamplerStrategy.Constant
      case _ => throw new IllegalArgumentException()
    }
  }
  def pScene: Parser[Unit] = "scene=" ~ word ^^ {case id~s => scene = SceneBuilder.getScene(s, this.width, this.height)}

  def line: Parser[Unit] = pName | pWidth | pHeight | pBucketSize | pSamplesRoot | pSamplerStrategy | pScene
  // -------------------------------------------------------------------------
  var name: String = ""
  var width: Int = -1
  var height: Int = -1
  var bucketSize: (Int, Int) = (-1, -1)
  var samplesRoot: Int = -1
  var samplerStrategy: SamplerStrategy = null
  var scene: Scene = null
}

object ConfigParser {
  def parse(filePath: String): RenderSettings = {
    val parser = new ConfigParser()
    for (line <- io.Source.fromFile(filePath).getLines) parser.parse(parser.line, line)
    new RenderSettings(parser.name,
                       parser.bucketSize,
                       parser.scene,
                       parser.samplesRoot,
                       parser.samplerStrategy)
  }
}
