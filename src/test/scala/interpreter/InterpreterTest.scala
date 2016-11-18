package interpreter

import ast._
import helpers.{ConcreteVariables, InterpreterTestBase}
import interpreter.structures.AllocableTree

class InterpreterTest extends InterpreterTestBase {
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

  it should "allocate declared tree variable to variables and allocate it on heap" in new AllStubbed {
    val instructions = Seq(new TreeVarDecl(id('myTree)))
    (heap.add _).when(AllocableTree.uninitialized).returns(pointer)

    runAll()

    (variables.put _).verify("myTree", pointer)
  }

  it should "allocate declared string variable to variables and allocate it on heap" in new AllStubbed {
    val instructions = Seq(new StringVarDecl(id('myString), "CONTENT"))
    (heap.add _).when(string("CONTENT")).returns(pointer)

    runAll()

    (variables.put _).verify("myString", pointer)
  }

  it should "allocate null string variable to variables" in new AllStubbed {
    val instructions = Seq(new StringVarDecl(id('myNullString), null))
    (heap.add _).when(allocableNull).returns(nullPointer)

    runAll()

    (variables.put _).verify("myNullString", nullPointer)
  }

  it should "print string from reference" in new AllConcrete {
    val instructions = Seq(
      new StringVarDecl(id('textVar), "Some text"),
      new PrintRef(id('textVar))
    )

    runAll()

    (output.printLine _).verify("Some text").once()
  }

  it should "print string null from reference" in new AllConcrete {
    val instructions = Seq(
      new StringVarDecl(id('textVar), null),
      new PrintRef(id('textVar))
    )

    runAll()

    (output.printLine _).verify(nullString).once()
  }

  it should "assign null to tree variable" in new AllConcrete {
    val instructions = Seq(
      new TreeVarDecl(id('myTree)),
      new NullAssignment(id('myTree))
    )

    runAll()

    variables.get("myTree") shouldBe nullPointer
    loneHeapValue shouldBe AllocableTree.uninitialized
  }

  it should "assign int constant to tree data" in new AllConcrete {
    val instructions = Seq(
      new TreeVarDecl(id('myTree)),
      new IntConstAssignment(ref("myTree.data"), 123)
    )

    runAll()

    loneHeapValue shouldBe tree(data = 123)
  }

  it should "assign int constant to nested tree data" in new AllConcrete {
    val instructions = Seq(
      new TreeVarDecl(id('myTree)),
      new TreeVarDecl(id('nestedTree)),
      new RefAssignment(ref("myTree.f1"), id('nestedTree)),
      new IntConstAssignment(ref("myTree.f1.data"), 123)
    )

    runAll()

    heapPairs should contain only (
      1 -> tree(2, nullPointer, data = 0),
      2 -> tree(nullPointer, nullPointer, data = 123)
    )
  }

  it should "assign string constant allocating it" in new AllConcrete {
    val instructions = Seq(
      new StringVarDecl(id('myString), "STRING1"),
      new StringConstAssignment(id('myString), "STRING2")
    )

    runAll()

    variable('myString) shouldBe 2
    heapPairs should contain only (
      1 -> string("STRING1"),
      2 -> string("STRING2")
    )
  }

  it should "assign string references" in new AllConcrete {
    val instructions = Seq(
      new StringVarDecl(id('s1), "A"),
      new StringVarDecl(id('s2), "B"),
      new RefAssignment(id('s1), id('s2))
    )

    runAll()

    variable('s1) shouldBe 2
    variable('s2) shouldBe 2
    heapPairs should contain only (
      1 -> string("A"),
      2 -> string("B")
    )
  }

  it should "assign tree references" in new AllConcrete {
    val instructions = Seq(
      new TreeVarDecl(id('t1)),
      new TreeVarDecl(id('t2)),
      new TreeVarDecl(id('t3)),
      new TreeVarDecl(id('t4)),
      new RefAssignment(ref("t1.f1"), id('t2)),
      new RefAssignment(ref("t1.f2"), id('t3)),
      new RefAssignment(ref("t1.f1.f2"), id('t4)),
      new RefAssignment(ref("t4.f1"), id('t1)),
      new RefAssignment(id('t3), id('t1))
    )

    runAll()

    variable('t1) shouldBe 1
    variable('t2) shouldBe 2
    variable('t3) shouldBe 1
    variable('t4) shouldBe 4
    heapPairs should contain only (
      1 -> tree(left = 2, right = 3),
      2 -> tree(right = 4, data = 0),
      3 -> tree(),
      4 -> tree(left = 1)
    )
  }

  it should "assign null to string variable" in new AllConcrete {
    val instructions = Seq(
      new StringVarDecl(id('str), "A"),
      new NullAssignment(id('str))
    )

    runAll()

    variable('str) shouldBe nullPointer
    loneHeapValue shouldBe string("A")
  }

  it should "assign null to nested tree" in new AllConcrete {
    val instructions = Seq(
      new TreeVarDecl(id('t1)),
      new TreeVarDecl(id('t2)),
      new TreeVarDecl(id('t3)),
      new TreeVarDecl(id('t4)),
      new RefAssignment(ref("t1.f1"), id('t2)),
      new RefAssignment(ref("t2.f1"), id('t3)),
      new RefAssignment(ref("t3.f1"), id('t4)),
      new NullAssignment(ref("t1.f1.f1.f1"))
    )

    runAll()

    heapPairs should contain only (
      1 -> tree(left = 2),
      2 -> tree(left = 3),
      3 -> tree(left = nullPointer),
      4 -> tree()
    )
  }

  it should "analyze heap and collect garbage" in new Fixture with StubHeap with ConcreteVariables {
    val instructions = Seq(
      new HeapAnalyze,
      new Collect,
      new HeapAnalyze
    )

    runAll()

    inSequence {
      (heap.analyze _).verify().once()
      (heap.collect _).verify().once()
      (heap.analyze _).verify().once()
    }
  }
}
