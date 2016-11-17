package helpers

import ast.{Program, Statement}
import org.scalatest.FlatSpecLike
import parsing.Parser

trait ParserTestBase extends Test with FlatSpecLike {
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