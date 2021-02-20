package org.learningconcurrency
import java.util.concurrent.atomic._
import scala.annotation.tailrec

object FileManager extends App {

	sealed trait State
	class Idle extends State
	class Creating extends State
	class Copying(val n: Int) extends State
	class Deleting extends State

	class Entry(val isDir: Boolean) {
		val state = new AtomicReference[State](new Idle)
	}

	@tailrec def prepareForDelete(entry: Entry): Boolean = {
		val s0 = entry.state.get()
		s0 match {
			case i: Idle =>
				if (entry.state.compareAndSet(s0, new Deleting)) true
				else prepareForDelete(entry)
			case c: Creating =>
				log("FIle currently created, cannot delete."); false
			case c: Copying =>
				log("FIle currently copied, cannot delete."); false
			case d: Deleting => 
				false  
		}
	}
}