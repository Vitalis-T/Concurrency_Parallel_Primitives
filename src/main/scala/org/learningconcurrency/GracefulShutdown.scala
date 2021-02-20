package org.learningconcurrency
import scala.collection._

//In the graceful shutdown, one thread sets the condition for the termination and then calls 'notify' to wake
//up a 'worker' thread. The 'worker' thread then releases all its resources and terminates willingly.
//We first introduce a variable called 'terminated' that is true if the thread should be stopped

object GracefulShutdown extends App {

	private val tasks = mutable.Queue[() => Unit]()
	object Worker extends Thread {
		var terminated = false
		def pool(): Option[() => Unit] = tasks.synchronized {
			while (tasks.isEmpty && !terminated) tasks.wait()
			if (!terminated) Some(tasks.dequeue()) else None
		}

		import scala.annotation.tailrec
		@tailrec override def run() = pool() match {
			case Some(task) => task(); run()
			case None => 
		}

		def shutdown() = tasks.synchronized {
			terminated = true
			tasks.notify()
		}		
	}

	Worker.start()

	def asynchronous(body: => Unit) = tasks.synchronized {
		tasks.enqueue(() => body)
		tasks.notify()
	}
	asynchronous { log("Hello") }
	asynchronous { log("Vitalis!") }
	Thread.sleep(500)
	Worker.shutdown()
	
}