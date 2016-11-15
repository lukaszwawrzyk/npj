package helpers

import ast.{Identifier, Program, Statement}
import interpreter._
import interpreter.structures.{Allocable, AllocableString, Tree}
import org.scalatest.LoneElement

trait InterpreterTestBase extends Test with LoneElement {
  trait Fixture {
    def tree(left: Int = nullPointer, right: Int = nullPointer, data: Int = 0) = new Tree(left, right, data)
    def string(value: String) = new AllocableString(value)
    def id(value: Symbol) = new Identifier(value.name)
    def ref(value: String) = new Identifier(value)

    val allocableNull = argThat((_: Allocable) == null)
    val nullString = argThat((_: String) == null)

    val pointer = 123
    val nullPointer = 0

    import scala.collection.JavaConverters._
    val output = stub[Output]
    def variables: Variables
    def heap: Heap

    def instructions: Seq[Statement]
    lazy val program = new Program(instructions.asJava)
    lazy val interpreter = new Interpreter(program, output, variables, heap)

    def runAll(): Unit = {
      while (interpreter.hasNext) {
        interpreter.runNext()
      }
    }
  }

  trait ConcreteVariables {
    val variables = new MapVariables

    def variable(key: Symbol) = variables.get(key.name)
  }

  trait ConcreteHeap { this: Fixture =>
    val heap = new Heap {
      var pointers = Map[Int, Allocable](nullPointer -> null)

      override def get(pointer: Int): Allocable = pointers(pointer)

      override def add(obj: Allocable): Int = {
        val pointer = pointers.keys.max + 1
        pointers += (pointer -> obj)
        pointer
      }

      override def put(pointer: Int, obj: Allocable): Unit = {
        pointers += (pointer -> obj)
      }
    }

    def loneHeapValue = heapValues.loneElement

    def heapPairs = heap.pointers.filter(_._1 != nullPointer).toSeq

    def heapValues = heapPairs.map(_._2)
  }

  trait StubVariables {
    val variables = stub[Variables]
  }

  trait StubHeap {
    val heap = stub[Heap]
  }

  trait AllStubbed extends Fixture with StubVariables with StubHeap

  trait AllConcrete extends Fixture with ConcreteHeap with ConcreteVariables
}
