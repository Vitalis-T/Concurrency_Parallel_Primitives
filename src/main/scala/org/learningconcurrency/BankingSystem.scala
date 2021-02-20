package org.learningconcurrency
/*
object ThreadsSleep extends App {
	val t = thread {
		Thread.sleep(1000)
		log("Still running.")
		Thread.sleep(1000)
		log("Still running.")
		Thread.sleep(1000)
		log("Completed")
	}
	t.join()
	log("New thread joined.")
}

object ThreadNodeterminism extends App {
	val t = thread {log("New thread running.")}
	log("...")
	log("...")
	t.join()
	log("New thread joined.")
} 

object ThreadsCommunicate extends App {
	var result: String = null
	val t = thread { result = "\nTitle\n" + "=" * 5 }
	t.join()
	log(result)
} */

object SynchronizedNesting extends App {
	import scala.collection._
	private val transfers = mutable.ArrayBuffer[String]()
	def logTransfer(name: String, n: Int) = transfers.synchronized {
		transfers += s"transfer to account '$name' = $n"
	}

	class Account(val name: String, var money: Int)

	def add(account: Account, n: Int) = account.synchronized {
		account.money += n
		if (n > 10) logTransfer(account.name, n)
	}
  
  val jane = new Account("Jane", 100)
  val john = new Account("John", 200)
  val t1 = thread { add(jane, 5) }
  val t2 = thread { add(john, 50) }
  val t3 = thread { add(jane, 75) }
  t1.join()
  log("jane-5")
  t2.join()
  log("john-50")
  t3.join()
  log("jane-75")
  log(s"----transfers----\n$transfers")
}

