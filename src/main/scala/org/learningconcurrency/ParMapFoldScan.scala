package org.learningconcurrency

object ParallelMapFoldScan {
	val threshold = 10000
	def mapSeqSeg[A, B](inp: Array[A], left: Int, right: Int, f: A => B, out: Array[B]): Unit = {
		// Writes to out(i) for left <= i <= right - 1
		var i = left
		while (i < right) {
			out(i) = f(inp(i))
			i += 1
		}
	}

	def mapParSeg[A, B](inp: Array[A], left: Int, right: Int, f: A => B, out: Array[B]): Unit = {
		if (right - left < threshold)
			mapSeqSeg(inp, left, right, f, out)
		else {
			val mid = left + (right - left) / 2
			val _ = parallel(mapSeqSeg(inp, left, mid, f, out),
			mapSeqSeg(inp, mid, right, f, out))
		}
	}
}