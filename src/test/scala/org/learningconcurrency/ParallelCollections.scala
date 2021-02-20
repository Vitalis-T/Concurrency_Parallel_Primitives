//package org.learningconcurrency

/*
import scala.collection._
object ParallelCollections extends App {

  /* Seq[T] – an ordered sequence of elements with type T.
	For code that is agnostic about parallelism, there exists a separate hierarchy of generic collection traits 
	GenIterable[T] , GenSeq[T] , GenSet[T] and GenMap[K, V]. */
	def largestPalindrome(xs: GenSeq[Int]): Int = {
		xs.aggregate(Int.MinValue)(
			(largest, n) => if (n > largest && n.toString == n.toString.reverse) n else largest,
			math.max)
	}

	//Computing Set Intersection(Side-Effecting Operation with this implementation)
	def intersection(a: GenSet[Int], b: GenSet[Int]): Set[Int] = {
		val result = mutable.Set[Int]()
		for (x <- a) if (b contains x) result += x
		result
	}

	val arr = (0 until 1000000).toArray
	val res1 = largestPalindrome(arr)
	val res2 = largestPalindrome(arr.par)
	//log(s"Largest Palindrome in array: $res1")
	//log(s"Largest Palindrome in parallel array: $res2")
	val seqRes = intersection((0 until 100000).toSet, (0 until 100000 by 4).toSet)
	val parRes = intersection((0 until 100000).par.toSet, (0 until 100000 by 4).par.toSet)
	log(s"Sequential result - ${seqRes.size}")
	log(s"Parallel result - ${parRes.size}")
}
*/