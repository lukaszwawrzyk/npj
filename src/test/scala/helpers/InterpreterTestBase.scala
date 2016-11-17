package helpers

import ast.{Identifier, Program, Statement}
import interpreter.structures.Allocable
import interpreter.{Heap, _}
import org.scalatest.{FlatSpecLike, LoneElement}


trait InterpreterTestBase extends Test with FlatSpecLike with LoneElement {
  trait Fixture extends AllocableFactories {
    def id(value: Symbol) = new Identifier(value.name)
    def ref(value: String) = new Identifier(value)

    val allocableNull = argThat((_: Allocable) == null)
    val nullString = argThat((_: String) == null)

    val pointer = 123

    import scala.collection.JavaConverters._
    val output = stub[Output]
    def variables: Variables
    def heap: Heap

    def instructions: Seq[Statement]
    lazy val program = new Program(instructions.asJava)
    lazy val interpreter = new Interpreter(program, output, variables, heap)

    def runAll(): Unit = {
      interpreter.runProgram()
    }
  }

  trait ConcreteHeap { this: Fixture =>
    class MapHeap extends Heap {
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

      override def analyze(): Unit = ()

      override def collect(): Unit = ()
    }

    val heap = new MapHeap

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
