package helpers

import java.util

import ast.{Program, Statement}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}
import parsing.Parser

trait Test extends FlatSpec with Matchers with MockFactory {
  protected def list[A](elems: A*) = util.Arrays.asList(elems: _*)
}

trait ParserTestBase extends Test {
  protected def parsing(text: String, expected: Program): Unit = {
    val parser = new Parser
    val result = parser.parse(text)
    result should be ('successful)
    result.getParsedResult shouldBe expected
  }

  protected def parsing(text: String)(statements: Statement*): Unit = {
    parsing(text, new Program(list(statements: _*)))
  }


  protected def parsingFailed(text: String)(expected: String) = {
    val parser = new Parser
    val result = parser.parse(text)
    result should not be 'successful
    result.getErrorMessage shouldBe expected
  }
}