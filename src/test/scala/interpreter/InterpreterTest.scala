package interpreter

import ast._
import helpers.InterpreterTestBase
import interpreter.structures.{AllocalbeString, Tree}
import org.scalatest.LoneElement

class InterpreterTest extends InterpreterTestBase with LoneElement {
  behavior of "Interpreter"

  it should "print constant" in new AllStubbed {
    val instructions = Seq(new PrintConst("Constant"))

    runAll()

    (output.printLine _).verify("Constant").once()
  }

  it should "print multiple constants" in new AllStubbed {
    val instructions = Seq(
      new PrintConst("1"),
      new PrintConst("2"),
      new PrintConst("3"),
      new PrintConst("1")
    )

    runAll()

    inSequence {
      (output.printLine _).verify("1").once()
      (output.printLine _).verify("2").once()
      (output.printLine _).verify("3").once()
      (output.printLine _).verify("1").once()
    }
  }

  it should "add declared tree variable to variables and allocate it on heap" in new AllStubbed {
    val instructions = Seq(new TreeVarDecl(new Identifier("myTree")))
    (heap.add _).when(Tree.uninitialized).returns(pointer)

    runAll()

    (variables.put _).verify("myTree", pointer)
  }

  it should "add declared string variable to variables and allocate it on heap" in new AllStubbed {
    val instructions = Seq(new StringVarDecl(new Identifier("myString"), "CONTENT"))
    (heap.add _).when(AllocalbeString.of("CONTENT")).returns(pointer)

    runAll()

    (variables.put _).verify("myString", pointer)
  }

  it should "add null string variable to variables" in new AllStubbed {
    val instructions = Seq(new StringVarDecl(new Identifier("myNullString"), null))
    (heap.add _).when(allocableNull).returns(nullPointer)

    runAll()

    (variables.put _).verify("myNullString", nullPointer)
  }

  it should "print string from reference" in new AllConcrete {
    val instructions = Seq(
      new StringVarDecl(new Identifier("textVar"), "Some text"),
      new PrintRef(new Identifier("textVar"))
    )

    runAll()

    (output.printLine _).verify("Some text").once()
  }

  it should "print string null from reference" in new AllConcrete {
    val instructions = Seq(
      new StringVarDecl(new Identifier("textVar"), null),
      new PrintRef(new Identifier("textVar"))
    )

    runAll()

    (output.printLine _).verify(nullString).once()
  }

  it should "assign null to tree variable" in new AllConcrete {
    val instructions = Seq(
      new TreeVarDecl(new Identifier("myTree")),
      new NullAssignment(new Identifier("myTree"))
    )

    runAll()

    variables.get("myTree") shouldBe nullPointer
    heap.pointers.collect { case (key, value) if key != 0 => value }.loneElement shouldBe Tree.uninitialized
  }

  it should ""
}
