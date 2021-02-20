package org.learningconcurrency

object SynchronizedGuardedBlocks extends App {
	val lock = new AnyRef
	var message: Option[String] = None
	val greeter = thread {
		lock.synchronized {
			while (message == None) lock.wait()
			log(message.get)
		}
	}

	lock.synchronized {
		message = Some("Hello, Vitalis")
		lock.notify()
	}
	greeter.join()
}