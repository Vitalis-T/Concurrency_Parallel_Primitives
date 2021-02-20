package org.learningconcurrency
import scala.concurrent._
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.TimeUnit

object ExecutorsCreate extends App {
	val executor = new ForkJoinPool
	executor.execute(new Runnable {
		def run() = log("This task is run asynchronously.")
	})
	Thread.sleep(500)
	log("It's a main thread of this application")
	// The 'shutdown' method makes sure that the 'Executor' object gracefully terminates by executing
	// all the submitted tasks and then stopping all the worker threads.
	executor.shutdown()
}


// The 'ExecutionContext' trait offers a similar functionality to that of 'Executor' objects but is more specific to Scala.
// Execution contexts implement the abstract 'execute' method, which exactly corresponds to the 'execute' method 
// on the 'Executor' interface.
// The 'ExecutionContext' companion object contains the default execution context called 'global' , 
// which internally uses a 'ForkJoinPool' instance.

object ExecutionContextGlobal extends App {
	val ectx = ExecutionContext.global
	ectx.execute(new Runnable {
		def run() = 
			log("Running on the execution context")
	})
}

/* The ExecutionContext companion object defines a pair of methods, 'fromExecutor' and
'fromExecutorService' , which create an ExecutionContext object from an Executor or
ExecutorService interface, respectively */
object ExecutionContextCreateFrom extends App {
	val pool = new ForkJoinPool(2)
	val ectx = ExecutionContext.fromExecutorService(pool)
	ectx.execute(new Runnable {
		def run() = 
			log("Running on the execution context again")
	})
	Thread.sleep(1000)
}
