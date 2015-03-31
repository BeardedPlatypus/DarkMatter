package com.beardedplatypus.util

import scala.collection.mutable
import scala.collection.mutable.HashMap

object PerformanceReporter {
  var testValues: HashMap[String, (Int, Double)] = new mutable.HashMap[String, (Int, Double)]()

  def time[R](block: => R, name: String): R = {
    val t0 = System.nanoTime()
    val result = block    // call-by-name
    val t1 = System.nanoTime()

    if (!testValues.contains(name)) testValues.put(name, (0, 0.0))
    testValues(name) = (testValues(name)._1 + 1, testValues(name)._2 + (t1 - t0))
    result
  }

  def getReport: String = {
    var res = ""
    for (v <- testValues) res = res + (v + ": count: " + v._2._1.toString + " | total time (s): " +  (v._2._2 / 1000000000.0).toString + " | average (s): " + ((v._2._2 / v._2._1) / 1000000000.0).toString + "\n");
    res
  }
}