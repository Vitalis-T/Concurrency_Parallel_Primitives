package org.learningconcurrency

import org.scalameter.api._
import org.scalameter.picklers.Implicits._

object RangeBenchmark
extends Bench.LocalTime {

  val sizes = Gen.range("size")(300000, 1500000, 300000)

  val ranges = for {
    size <- sizes
  } yield 0 until size

  performance of "Range" in {
    measure method "map" in {
      using(ranges) in {
        r => r.map(_ + 1)
      }
    }
  }
}

object ModifiedRangeBenchmark extends Bench[Double] {
  // configuration
  lazy val executor = LocalExecutor(
    new Executor.Warmer.Default,
    Aggregator.min[Double], 
    measurer)
  lazy val measurer = new Measurer.Default
  lazy val reporter = new LoggingReporter[Double]
  lazy val persistor = Persistor.None

  /* inputs */

  val sizes = Gen.range("size")(300000, 1500000, 300000)

  val ranges = for {
    size <- sizes
  } yield 0 until size

  /* tests */

  performance of "RangeTwo" in {
    measure method "mapTwo" in {
      using(ranges) in {
        r => r.map(_ + 1)
      }
    }
  }
}