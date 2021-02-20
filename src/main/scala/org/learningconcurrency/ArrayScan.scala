/*package org.learningconcurrency
object ArrayScan {

	//Intermediate tree for array reduce
	//The only difference compared to previous TreeRes: each Leaf now keeps track of the array segment range (from, to) 
	//from which res is computed.
	sealed abstract class TreeResArr[A] {val res: A}
	case class Leaf[A](from: Int, to: Int,
										 override val res: A) extends TreeResArr[A]

	case class Node[A](l: TreeResArr[A], override val res: A, r: TreeResArr[A]) extends TreeResArr[A]

	//Starts from an array, produces a tree
	def upsweep[A](inp: Array[A], from: Int, to: Int, f: (A, A) => A): TreeResArr[A] = {
		if (to - from < threshold)
			Leaf(from, to, reduceSeg(inp, from + 1, to, inp(from), f))
		else {
			mid = from + (to - from) / 2
			val (tL, tR) = parallel(upsweep(inp, from, mid, f), upsweep(inp, mid, to, f))
			Node(tL, f(tL.res, tR.res), tR)
		}
	}

	def reduceSeg[A](inp: Array[A], left: Int, right: Int, a0: A, f: (A, A) => A): A = {
		var a = a0
		var i = left
		while (i < right) {
			a = (f(a, inp(i)))
			i += 1
		}
		a
	}

	//Downsweep on array
	def downsweep[A](inp: Array[A], a0: A, f: (A, A) => A,
                   t: TreeResArr[A], out: Array[A]): Unit = t match {
   	case Leaf(from, to, res) => scanLeftSeg(inp, from, to, a0, f, out)
   	case Node(l, _, r) => {
   		val(_,_) = parallel(downsweep(inp, a0, f, l, out), downsweep(inp, f(a0, l.res), f, r, out))
   	} 
   }
  //Sequential scan left on segment. Writes to output shifted by one.
  def scanLeftSeg[A](inp: Array[A], left: Int, right: Int, a0: A,
   										f: (A, A) => A, out: Array[A]) = {
  	if (left < right) {
  		var i = left
  		var a = a0
  		while (i < right) {
  			a = f(a, inp(i))
  			i += 1
  			out(i) = a
  		}
  	}
  }

  def scanLeft[A](inp: Array[A],
									a0: A, f: (A,A) => A,
									out: Array[A]) = {
		val t = upsweep(inp, 0, inp.length, f)
		downsweep(inp, a0, f, t, out) // fills out[1..inp.length]
		out(0)= a0 // prepends a0
		}

}
*/

