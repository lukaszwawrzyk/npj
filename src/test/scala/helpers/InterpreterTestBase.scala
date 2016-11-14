package helpers

import ast.{Program, Statement}
import interpreter._
import interpreter.structures.Allocable

trait InterpreterTestBase extends Test {
  trait Fixture {
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
    }
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
