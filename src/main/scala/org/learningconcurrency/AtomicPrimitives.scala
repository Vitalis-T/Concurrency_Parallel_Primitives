package org.learningconcurrency
import java.util.concurrent.atomic._
import scala.annotation.tailrec

// Atomic Variables
object AtomicUid extends App {
  private val uid: AtomicLong = new AtomicLong(0L)
  def getUniqueId(): Long = uid.incrementAndGet()
  execute{ log(s"Uid asynchronously: ${getUniqueId()}") }
  log(s"Got a unique id: ${getUniqueId()}")
}

object AtomicCASUid extends App {
  private val uid: AtomicLong = new AtomicLong(0L)

  @tailrec def getUid(): Long = {
    val oldUid = uid.get
    val newUid = oldUid + 1
    if (uid.compareAndSet(oldUid, newUid)) 
      newUid
    else 
      getUid()
  }

  execute(log(s"Got a unique id asynchronously: ${getUid()}"))

  log(s"Got a unique id: ${getUid()}")
}


/*
However, not all operations composed from atomic primitives are lock-free. Using atomic variables is a necessary 
precondition for lock-freedom, but it is not sufficient. 
To show this, we will implement our own simple synchronized statement, which will use atomic variables.
The main difference with respect to the 'synchronized' statement is that threads calling 'mySynchronized' busy-wait 
in the while loop until the lock becomes available. 
Such locks are dangerous and much worse than the synchronized statement.
*/
object AtomicLock extends App {
  private val lock = new AtomicBoolean(false)

  def mySynchronized(body: => Unit): Unit = {
    while (!lock.compareAndSet(false, true)) {}
    try body 
    finally lock.set(false)
  } 

  var count = 0
  for (i <- 0 until 10) execute { mySynchronized{ count += 1 } }
  Thread.sleep(2000)
  log(s"Count is: $count")
}