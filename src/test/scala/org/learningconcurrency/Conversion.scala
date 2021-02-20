//package org.learningconcurrency

/*
import org.scalameter._
object Conversion {
	// Non-Parallelizable Collection and their conversions to Parallelizable
	val vector = Vector.fill(10000000)("") //creates a ParVector[String]
	val list = vector.toList //also creates a ParVector[String]

	val standardConfig = config(
		Key.exec.minWarmupRuns -> 10,
		Key.exec.maxWarmupRuns -> 20,
		Key.exec.benchRuns -> 20,
		Key.verbose -> true) withWarmer(new Warmer.Default)

	val memConfig = config(
		Key.exec.minWarmupRuns -> 0,
		Key.exec.maxWarmupRuns -> 0,
		Key.exec.benchRuns -> 10,
		Key.verbose -> true) withWarmer(Warmer.Zero)

	def main(args: Array[String]) {
		val listTime = standardConfig measure {
			list.par
		}
		println(s"List conversion time: $listTime")

		val vectorTime = standardConfig measure {
			vector.par
		}
		println(s"Vector conversion time: $vectorTime") 

		//println(s"Difference: ${listTime / vectorTime}")
	}

}
*/