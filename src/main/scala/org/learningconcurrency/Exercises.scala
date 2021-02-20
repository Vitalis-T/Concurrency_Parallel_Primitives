package org.learningconcurrency 

object ScanLeft {
	def scanLeft[A](inp: Array[A], a0: A, f : (A, A) => A, out : Array[A]): Unit = {
		val l = inp.length
		var i = 0
		out(0) = a0
		var a = a0
		while (i < l) {
			a = f(inp(i), a)
			i += 1
			out(i) = a			
		}
	}

	//Making scanLeft parallel. We will reuse intermediate results of 'reduce'.
	//Idea: save the intermediate results of this parallel computation.
	//We first assume that input collectio is also (another) tree.

	//Trees storing our input collection only have values in leaves
	sealed abstract class Tree[A]
	case class Leaf[A](a: A) extends Tree[A]
	case class Node[A](l: Tree[A], r: Tree[A]) extends Tree[A]

	 //Trees storing intermediate values also have (res) values in nodes
	sealed abstract class TreeRes[A] {val res: A}
	case class LeafRes[A](override val res: A) extends TreeRes[A]
	case class NodeRes[A](l: TreeRes[A], override val res: A, r: TreeRes[A])  extends TreeRes[A]

	 //Define 'reduceRes' function that transforms 'Tree' into 'TreeRes'
	def reduceRes[A](t: Tree[A], f: (A, A) => A): TreeRes[A] = t match {
	  case Leaf(v) => LeafRes(v)
	 	case Node(l, r) => {
	 	  val (tL, tR) = (reduceRes(l, f), reduceRes(r, f))
	 		NodeRes(tL, f(tL.res, tR.res), tR)
	 	}
	}
	 //Parallel 'reduce' that preserves the computation tree (upsweep)
	def upsweep[A](t: Tree[A], f: (A, A) => A): TreeRes[A] = t match {
	  case Leaf(v) => LeafRes(v)
	 	case Node(l, r) => {
	 	  val (tL, tR) = parallel(upsweep(l, f), upsweep(r, f))
	 		NodeRes(tL, f(tL.res, tR.res), tR)
	 	}
	}

   //Using tree with results to create the final collection
   //'a0' is reduce of all elements left of the tree ’t’
	def downsweep[A](t: TreeRes[A], a0: A, f: (A, A) => A): Tree[A] = t match {
	  case LeafRes(a) => Leaf(f(a0, a))
	 	case NodeRes(l, _, r) => {
	 	  val (tL, tR) = parallel(downsweep(l, a0, f), downsweep(r, f(a0, l.res), f))
	 		Node(tL, tR)
	 	}
	}

	// 'scanLeft' on Trees
	def scanLeftTree[A](t: Tree[A], a0: A, f: (A, A) => A): Tree[A] = {
		val tRes = upsweep(t, f)
		val scan1 = downsweep(tRes, a0, f)
		prepend(a0, scan1)
	}

	def prepend[A](x: A, t: Tree[A]): Tree[A] = t match {
		case Leaf(v) => Node(Leaf(x), Leaf(v))
		case Node(l, r) => Node(prepend(x, l), r)  
	}


	def countChange(money: Int, coins: List[Int]): Int = 
	  if (money < 0 || coins.isEmpty) 
	  	0
	  else if (money == 0)
	  	1
	  else countChange(money - coins.head, coins) + countChange(money, coins.tail)


}
/*
import scalameter._
val t2 = Node(Node(Leaf(1), Node(Leaf(3), Leaf(5))), Node(Node(Leaf(10), Leaf(20)), Leaf(30)))
val plus = (x: Int, y: Int) => x + y

withWarmer(new Warmer.Default) measure {
  reduceRes(t2, plus)
 }

withWarmer(new Warmer.Default) measure {
  upsweep(t2, plus)
}*/



