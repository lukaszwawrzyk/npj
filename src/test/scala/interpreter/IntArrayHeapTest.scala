package interpreter

import helpers.HeapTestBase

class IntArrayHeapTest extends HeapTestBase {
  "IntArrayHeap" when {
    "allocating objects" should {
      "retrieve string object back" in new Fixture {
        private val stringObject = string("ABCD")
        val pointer = heap.allocate(stringObject)

        val retrieved = heap.get(pointer)

        retrieved.asString shouldBe stringObject
      }

      "retrieve tree object back" in new Fixture {
        val treeObject = tree(left = 12, right = 31, data = 10)
        val pointer = heap.allocate(treeObject)

        val retrieved = heap.get(pointer)

        retrieved.asTree shouldBe treeObject
      }

      "return null pointer for null" in new Fixture {
        val pointer = heap.allocate(null)

        pointer shouldBe nullPointer
      }

      "place multiple objects one next to another" in new Fixture {
        val tree1 = tree(left = 123, right = 321, data = 5)
        val abcdString = string("abcd")
        val tree2 = tree(left = 1, right = 2, data = 3)

        val pointerToFirstTree = heap.allocate(tree1)
        val pointerToString = heap.allocate(abcdString)
        val pointerToSecondTree = heap.allocate(tree2)

        pointerToFirstTree shouldBe 1
        pointerToString shouldBe 5
        pointerToSecondTree shouldBe 11

        heap.get(pointerToFirstTree).asTree shouldBe tree1
        heap.get(pointerToString).asString shouldBe abcdString
        heap.get(pointerToSecondTree).asTree shouldBe tree2
      }

      "throw out of memory if requested object cannot fit in heap" in new Fixture {
        override val allocableHeapSpace = 4
        val x = allocable(size = 5)

        an [OutOfMemoryError] should be thrownBy heap.allocate(x)
      }

      "collect garbage to make space to allocate new object" in new Fixture {
        override val allocableHeapSpace = 10
        val x = allocable(size = 4)
        val y = allocable(size = 3)

        heap.allocate(x)
        heap.allocate(x)

        noException should be thrownBy heap.allocate(y)
      }

      "throw out of memory if requested object cannot fit after collecting garbage" in new Fixture {
        override val allocableHeapSpace = 10
        val x = allocable(size = 4)
        val tooBig = allocable(size = 7)

        variables.put('x, heap.allocate(x))
        heap.allocate(x)

        an [OutOfMemoryError] should be thrownBy heap.allocate(tooBig)
      }
    }

    "replacing" should {
      "replace current object with requested one of the same type" in new Fixture {
        val tree1 = tree(left = 123, right = 321, data = 5)
        val tree2 = tree(left = 1, right = 2, data = 3)
        val pointerToFirstTree = heap.allocate(tree1)
        val pointerToSecondTree = heap.allocate(tree2)

        val updatedTree = tree(left = 6, right = 7, data = 8)
        heap.update(pointerToSecondTree, updatedTree)

        heap.get(pointerToFirstTree).asTree shouldBe tree1
        heap.get(pointerToSecondTree).asTree shouldBe updatedTree
      }
    }

    "analyzing" should {
      "find all allocated objects" in new Fixture {
        heap.allocate(tree(data = 1))
        heap.allocate(string("str"))
        heap.allocate(tree(data = 2))
        heap.allocate(tree(data = 4))
        heap.allocate(string("x"))

        heapShouldContainOnly(1, 2, 4)("str", "x")
      }

      "find objects if they have duplicated values" in new Fixture {
        heap.allocate(tree(data = 1))
        heap.allocate(tree(data = 1))
        heap.allocate(tree(data = 1))
        heap.allocate(string("a"))
        heap.allocate(string("a"))

        heapShouldContainOnly(1, 1, 1)("a", "a")
      }
    }

    "collecting garbage" should {
      "clear everything if no pointers exist in variables" in new Fixture {
        val x = heap.allocate(tree(data = 1))
        val y = heap.allocate(tree(data = 2, left = x))
        val z = heap.allocate(tree(data = 3, left = y))
        val a = heap.allocate(string("a"))
        val b = heap.allocate(string("b"))

        heap.collect()

        heapShouldBeEmpty()
      }

      "collect objects not referenced from any variables and keep those which are referenced" in new Fixture {
        val x = heap.allocate(tree(data = 1))
        val y = heap.allocate(tree(data = 2))
        val a = heap.allocate(string("a"))
        val b = heap.allocate(string("b"))
        variables.put('x, x)
        variables.put('a, a)

        heap.collect()

        heapShouldContainOnly(1)("a")
      }

      "copy object that is referenced by two variables only once" in new Fixture {
        val x = heap.allocate(tree(data = 1))
        variables.put('x1, x)
        variables.put('x2, x)

        heap.collect()

        heapShouldContainOnly(1)()
      }

      "handle complex scenario" in new Fixture {
        val x = heap.allocate(tree(data = 1))
        val y = heap.allocate(tree(data = 2, left = x))
        val z = heap.allocate(string("str"))
        val b = heap.allocate(string("x"))
        variables.put('y, y)
        variables.put('z, z)

        heap.collect()

        val yUpdated = heap.get(variables.get('y)).asTree
        val zUpdated = heap.get(variables.get('z)).asString
        val xUpdated = heap.get(yUpdated.getLeftPointer).asTree

        xUpdated.getData shouldBe 1
        yUpdated.getData shouldBe 2
        zUpdated.getValue shouldBe "str"

        checkDifferent(
          variables.get('y) -> y,
          variables.get('z) -> z,
          yUpdated.getLeftPointer -> x
        )
        heapShouldContainOnly(1, 2)("str")
      }
    }
  }
}
