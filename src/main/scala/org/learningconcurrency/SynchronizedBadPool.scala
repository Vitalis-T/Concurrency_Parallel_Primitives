package org.learningconcurrency
import scala.collection._

//The 'worker' thread is busy-waiting(it is repetitively checking if there are any tasks on the queue) and this cause needlessly CPU performance.
//We are running this example from SBT in the same JVM process that SBT itself is running. 
//Becouse SBT has no-daemon threads of its own, our 'worker' thread is not stopped.
//To tell SBT that it should execute the run command in a new process, enter the following directive: set fork := true
//Running this example again should stop the 'worker' thread as soon as the main thread completes its execution.

object SynchronizedBadPool extends App {
	private val tasks = mutable.Queue[() => Unit]()

	val worker = new Thread {
		def pool(): Option[() => Unit] = tasks.synchronized {
			if (tasks.nonEmpty) Some(tasks.dequeue()) else None
		}

		override def run() = while(true) pool() match {
			case Some(task) => task()
			case None => 
		}
	}

	worker.setName("Worker")
	worker.setDaemon(true)
	worker.start()

	def asynchronous(body: => Unit) = tasks.synchronized {
		tasks.enqueue(() => body)
	}

	asynchronous({ log("Hello") })
	asynchronous({ log("world!") })
	Thread.sleep(5000)
}