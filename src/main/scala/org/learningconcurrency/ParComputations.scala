package org.learningconcurrency
import scala.util.Random

object ParComputations extends App {
  val threshold = 3

  def sumSegment(a: Array[Int], p: Double, s: Int, t: Int):Int = {
      var i = s; var sum: Int = 0
      while (i < t) {
        sum = sum + power(a(i), p)
        i += 1
      }
      sum
    }

  // Functional style
  def funcSumSegment(a: Array[Int], p: Double, s: Int, t: Int): Int = {
    def sumFrom(i: Int, acc: Int): Int = {
      if (i < t)
        sumFrom(i + 1, acc + power(a(i), p))
      else acc
    }
    sumFrom(s, 0) 
  }

  def power(x: Int, p: Double): Int = math.exp(p * math.log(math.abs(x))).toInt

  def pNorm(a: Array[Int], p: Double): Int = 
    power(sumSegment(a, p, 0, a.length), 1/p)

  def pNormTwoParts(a: Array[Int], p: Double): Int = {
    val middle = a.length / 2
    val (sum1, sum2) = (sumSegment(a, p, 0, middle), sumSegment(a, p, middle, a.length))
    power(sum1 + sum2, 1/p)
  }

  // Parallel version of the pNorms(added only parallel combinator)
  def pNormTwoPartsPar(a: Array[Int], p: Double): Int = {
    val middle = a.length / 2
    val (sum1, sum2) = parallel(sumSegment(a, p, 0, middle), sumSegment(a, p, middle, a.length))
    power(sum1 + sum2, 1/p)
  }

  //Process four array segments in parallel
  def pNormFourPartsPar(a: Array[Int], p: Double): Int = {
    val len = a.length
    val m1 = len / 4; val m2 = len / 2; val m3 = 3 * len / 4
    val ((sum1, sum2), (sum3, sum4)) = 
      parallel(parallel(sumSegment(a, p, 0, m1), sumSegment(a, p, m1, m2)),
        parallel(sumSegment(a, p, m2, m3), sumSegment(a, p, m3, len)))
    power(sum1 + sum2 + sum3 + sum4, 1/p)
  }

  // Recursive parallel 'sumSegment'
  def segmentRec(a: Array[Int], p: Double, s: Int, t: Int): Int = {
    if (t - s < threshold)
      sumSegment(a, p, s, t)
    else {
      val mid = a.length / 2
      val (leftSum, rightSum) = parallel(sumSegment(a, p, s, mid), sumSegment(a, p, mid, t))
      leftSum + rightSum
    }
  }
 // Measuring of the performance
  def currentTime() = System.currentTimeMillis()
  def deltaTime(t0: Long) = System.currentTimeMillis() - t0
  val arr = (1 to 1000000).toArray
  val startTime = currentTime()
  println(s"Sequential result: ${pNormTwoParts(arr, 2)}, time: ${deltaTime(startTime)}")
  val startTime2 = currentTime()
  println(s"Parallel result with 2 threads: ${pNormTwoPartsPar(arr, 2)}, time: ${deltaTime(startTime2)}")

  // I have only two cores on my computer, 
  // so in the case of 4 threads I don't get any speedup compare with two threads.
  val startTime3 = currentTime()
  println(s"Parallel result with 4 threads: ${pNormFourPartsPar(arr, 2)}, time: ${deltaTime(startTime3)}")




  // Monte-Carlo method for estimating pi value(Sequentional)
  /*def mcCount(iter: Int): Int = {
    val randomX = new Random
    val randomY = new Random
    var hits = 0
    for (i <- 0 until iter) {
      val x = randomX.nextDouble
      val y = randomY.nextDouble
      if (x * x + y * y < 1) hits += 1
    }
    hits
  }

  def monteCarloPiSeq(iter: Int): Double = 4.0 * mcCount(iter) / iter */

}

// Examples of the currying

object Currying {

  def currying3(f: Int => Int)(g: Int => Int)(a: Int, b: Int): Int = {
    if (a > b)
      0
    else
      g.compose(f)(a) + currying3(f)(g)(a+1, b)
  }

  def currying2(f: Int => Int)(g: Int => Int): (Int, Int) => Int = {
    def sumF(a: Int, b: Int): Int = {
      if(a > b) 0
      else
        g.compose(f)(a) + sumF(a+1, b)
    } 
    sumF
  }

  def currying1(f: Int => Int): (Int => Int) => (Int, Int) => Int = {
    def double(g: Int => Int): (Int, Int) => Int = {
      def sumF(a: Int, b: Int): Int = {
        if (a > b) 0
        else
          g.compose(f)(a) + sumF(a+1, b)
      }
      sumF
    }
    double
  }
}