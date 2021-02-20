package org.learningconcurrency
import scala.collection._

//A 'synchronized' statement in which some condition is repetitively checked before calling 'wait' method is called a guarded block.
//We can now use our insight on guarded blocks to avoid the busy-wait in our 'worker thread' in advance.

object  SynchronizedPool extends App {
  private val tasks = mutable.Queue[() => Unit]()
  object Worker extends Thread {
    setDaemon(true)
    def pool() = tasks.synchronized {
      while (tasks.isEmpty) tasks.wait()
      tasks.dequeue()
    }
    override def run() = while(true) {
      val task = pool()
      task()
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
}
