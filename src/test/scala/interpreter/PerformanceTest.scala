package interpreter

import helpers.AllocableFactories

import scala.util.Random

object PerformanceTest extends App with AllocableFactories {
  type Pointer = Int

  val generatedStrings = generateStrings(5000)
  val heapHalveSize = 10000000
  val variables = new MapVariables
  val heap = new IntArrayHeap(heapHalveSize, variables)

  val startTime = System.nanoTime()
  var cycle = 0
  loop {
    withStrings {
      withTrees {
        heap.collect()
      }
    }
    heap.collect()

    cycle += 1

    if (cycle % 100 == 0) {
      val now = System.nanoTime()
      val totalDuration = now - startTime
      val cyclesPerSec = cycle * 10e9 / totalDuration
      println(cyclesPerSec)
    }
  }


  def withTrees(block: => Unit) = {
    val tree1 = generateTree(14)
    val tree2 = generateTree(14)
    variables.put("x", tree1)
    variables.put("y", tree2)

    block

    variables.put("x", nullPointer)
    variables.put("y", nullPointer)
  }

  def withStrings(block: => Unit) = {
    val stringPointers = generatedStrings.map(heap.allocate)
    stringPointers.foreach(pointer => variables.put(pointer.toString, pointer))

    block

    stringPointers.foreach(pointer => variables.put(pointer.toString, nullPointer))
  }

  def generateStrings(number: Int) = {
    (1 to number).map(_.toString).map(appendRandomString).map(string)
  }

  def appendRandomString(string: String): String = {
    string + Random.alphanumeric.take(Random.nextInt(500)).mkString
  }

  def generateTree(depth: Int): Pointer = {
    if (depth > 0) {
      val newTree = tree(generateTree(depth - 1), generateTree(depth - 1), Random.nextInt())
      heap.allocate(newTree)
    } else {
      heap.allocate(tree(data = Random.nextInt()))
    }
  }

  def loop(body: => Unit): Unit = {
    body
    loop(body)
  }
}
