package helpers

import interpreter.structures.{AllocableString, AllocableTree}

trait AllocableFactories {
  val nullPointer = 0
  def tree(left: Int = nullPointer, right: Int = nullPointer, data: Int = 0) = new AllocableTree(left, right, data)
  def string(value: String) = new AllocableString(value)
  def allocable(size: Int) = {
    if (size <= 2) {
      throw new AssertionError("no such small allocable")
    } else if (size == 4) {
      tree()
    } else {
      string(List.fill(size - 2)("a").mkString)
    }
  }
}
