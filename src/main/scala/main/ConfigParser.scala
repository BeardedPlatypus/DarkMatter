package main

import com.beardedplatypus.main.{RenderSettings, SceneBuilder}
import com.beardedplatypus.math.Ray
import com.beardedplatypus.sampling.SamplerStrategy
import com.beardedplatypus.sampling.SamplerStrategy.SamplerStrategy
import com.beardedplatypus.shading.ShadingResult
import com.beardedplatypus.tracer.RenderKernel
import com.beardedplatypus.world.Scene

import scala.util.parsing.combinator.RegexParsers

// Reads basic .ini files to configure renderer
// TODO create a better way to implement this.
private class ConfigParser extends RegexParsers {
  def positiveInteger: Parser[Int] = """(0|[1-9]\d*)""".r ^^ { _.toInt }
  def word: Parser[String] = """([a-zA-Z0-9]+)""".r ^^ (_.toString)
  def samplerStrategyType: Parser[SamplerStrategy] = word ^^ {case strat => strat match {
    case "random" => SamplerStrategy.Random
    case "jittered" => SamplerStrategy.Jittered
    case "constant" => SamplerStrategy.Constant
    case _ => throw new IllegalArgumentException()
  }}

  // -------------------------------------------------------------------------
  def pName: Parser[Unit] = "name=" ~ word ^^ {case id~n => this.name = n}

  def pWidth: Parser[Unit] = "width=" ~ positiveInteger ^^ {case id~w => this.width = w}
  def pHeight: Parser[Unit] = "height=" ~ positiveInteger ^^ {case id~h => this.height = h}
  def pBucketSize: Parser[Unit] = "bucket_size=(" ~ positiveInteger ~ "," ~ positiveInteger ~ ")" ^^ {
    case id~bucketX~s~bucketY~e => bucketSize = (bucketX, bucketY)
  }
  def pSamplesRootAA: Parser[Unit] = "samples_root_aa=" ~ positiveInteger ^^ {case id~s => this.samplesRootAA = s}
  def pSamplerStrategyAA: Parser[Unit] = "sampler_strategy_aa=" ~ samplerStrategyType ^^ {case id~w => samplerStrategyAA = w}

  def pBranchingFactorRoot: Parser[Unit] = "branching_factor=" ~ positiveInteger ^^ {case id~s => this.branchingFactorRoot = s}
  def pMaxDepth: Parser[Unit] = "max_indirect_depth=" ~ positiveInteger ^^ {case id~d => this.maxDepthIndirect = d}
  def pSamplerStrategyGI: Parser[Unit] = "sampler_strategy_gi=" ~ samplerStrategyType ^^ {case id~w => samplerStrategyGI = w}

  def pKernel: Parser[Unit] = "kernel=" ~ word ^^ {case id~k => kernel = RenderKernel.getKernel(k)}
  def pScene: Parser[Unit] = "scene=" ~ word ^^ {case id~s => scene = SceneBuilder.getScene(s, this.width, this.height, maxDepthIndirect)}

  def line: Parser[Unit] = pName | pWidth | pHeight | pBucketSize | pSamplesRootAA | pSamplerStrategyAA | pBranchingFactorRoot | pMaxDepth | pSamplerStrategyGI | pKernel | pScene
  // -------------------------------------------------------------------------
  var name: String = ""
  var width: Int = -1
  var height: Int = -1
  var bucketSize: (Int, Int) = (-1, -1)
  var samplesRootAA: Int = -1
  var samplerStrategyAA: SamplerStrategy = null
  var renderTechnique: (IndexedSeq[Ray],Scene) => IndexedSeq[ShadingResult] = null
  var branchingFactorRoot: Int = -1
  var maxDepthIndirect: Int = -1
  var samplerStrategyGI: SamplerStrategy = null
  var kernel: (IndexedSeq[Ray],Scene) => IndexedSeq[ShadingResult] = null
  var scene: Scene = null
}

object ConfigParser {
  def parse(filePath: String): RenderSettings = {
    val parser = new ConfigParser()
    for (line <- io.Source.fromFile(filePath).getLines) parser.parse(parser.line, line)
    new RenderSettings(parser.name,
                       parser.bucketSize,
                       parser.kernel,
                       parser.scene,
                       parser.samplesRootAA,
                       parser.samplerStrategyAA,
                       parser.branchingFactorRoot,
                       parser.samplerStrategyGI)
  }
}
