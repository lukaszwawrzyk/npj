package helpers

import java.util

import org.scalamock.scalatest.MockFactory
import org.scalatest.Matchers

trait Test extends Matchers with MockFactory {
  protected def list[A](elems: A*) = util.Arrays.asList(elems: _*)
}