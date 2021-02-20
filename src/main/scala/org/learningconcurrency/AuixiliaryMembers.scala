package org
import scala.concurrent.ExecutionContext.global
package object learningconcurrency {
	
	def log(msg: String): Unit =
	  println(s"${Thread.currentThread.getName}: $msg")

	def thread(body: => Unit): Thread = {
		val t = new Thread {
			override def run(): Unit = body
				//log("I'm running")
		}
		t.start()
		t
	}

	var uidCount = 0L
	
	def getUniqueId() = this.synchronized{
		val freshUid = uidCount + 1
		uidCount = freshUid
		freshUid
	}

	def parallel[A, B](a: => A, b: => B): (A, B) = {
  	var taks1 = null.asInstanceOf[A]
  	var taks2 = null.asInstanceOf[B]
  	
		val t1 = thread {
			taks1 = a
			log(taks1.toString())
		}

		val t2 = thread {
			taks2 = b
			log(taks2.toString())
		}
		
		t1.join()
		t2.join()
		(taks1, taks2)
	}

	def execute(body: => Unit) = global.execute(
		new Runnable { def run() = body }
	)
}