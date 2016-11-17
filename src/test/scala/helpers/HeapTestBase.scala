package helpers

import interpreter.IntArrayHeap
import interpreter.structures.{Allocable, AllocableString, Tree}
import org.scalatest.{Inside, WordSpecLike}

import scala.collection.JavaConverters._
import scala.language.implicitConversions

trait HeapTestBase extends Test with WordSpecLike  with Inside {

  trait Fixture extends ConcreteVariables with AllocableFactories {
    val heap = new IntArrayHeap(40, variables)

    def heapShouldContainOnly(expectedInts: Int*)(expectedStrings: String*) = {
      val analyzed = heap.analyze
      val ints = analyzed.getTreeData.asScala.map(_.intValue)
      val strings = analyzed.getStrings.asScala

      ints should contain theSameElementsAs expectedInts
      strings should contain theSameElementsAs expectedStrings
    }

    def heapShouldBeEmpty() = {
      heapShouldContainOnly()()
    }
  }

  def checkDifferent[A](pairs: (A, A)*) = {
    pairs foreach { case (left, right) => left should not be right }
  }

  implicit class AllocableOps(val allocable: Allocable) {
    def asTree: Tree = allocable.asInstanceOf[Tree]
    def asString: AllocableString = allocable.asInstanceOf[AllocableString]
  }

  implicit def symbolToString(symbol: Symbol): String = symbol.name
}