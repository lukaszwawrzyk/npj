package helpers

import interpreter.structures.{AllocableString, Tree}

trait AllocableFactories {
  val nullPointer = 0
  def tree(left: Int = nullPointer, right: Int = nullPointer, data: Int = 0) = new Tree(left, right, data)
  def string(value: String) = new AllocableString(value)
}
