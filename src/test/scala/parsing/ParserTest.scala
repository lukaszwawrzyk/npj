package parsing


import ast._
import helpers.ParserTestBase

class ParserTest extends ParserTestBase {
  behavior of "Parser"

  it should "fail for wrong input" in parsingFailed("notvalidprogram") {
    ":("
  }

  it should "parse empty program" in parsing("", expected = new Program(list()))

  it should "parse multiple print const" in parsing {
    """Print "HELLO";
      |Print "WORLD";""".stripMargin
  }(
    new PrintConst("HELLO"),
    new PrintConst("WORLD")
  )

  it should "parse print reference" in parsing("Print identifier;")(new PrintRef(new Identifier("identifier")))

  it should "parse heap analyze and collect" in parsing {
    """HeapAnalyze;
      |Collect;""".stripMargin
  }(
    new HeapAnalyze,
    new Collect
  )

  it should "parse tree variable declaration" in parsing("VarDeclT ident123;") {
    new TreeVarDecl(new Identifier("ident123"))
  }

  it should "parse string variable declaration" in parsing("""VarDeclS ident123 "val";""") {
    new StringVarDecl(new Identifier("ident123"), "val")
  }

  it should "parse string variable declaration with null" in parsing("""VarDeclS ident123 NULL;""") {
    new StringVarDecl(new Identifier("ident123"), null)
  }

  it should "parse null assignment" in parsing("tree.f1.f2.f1.data = NULL;") {
    new NullAssignment(new Identifier("tree.f1.f2.f1.data"))
  }

  it should "parse reference assignment" in parsing("tree = t.f1.f2;") {
    new RefAssignment(new Identifier("tree"), new Identifier("t.f1.f2"))
  }

  it should "parse string constant assignment" in parsing("""string = "const";""") {
    new StringConstAssignment(new Identifier("string"), "const")
  }

  it should "parse int constant assignment" in parsing("num = 1234;") {
    new IntConstAssignment(new Identifier("num"), 1234)
  }

  it should "parse complex program" in parsing (
    """VarDeclT cycle;
      |cycle.f1 = cycle;
      |cycle.data = 23;
      |VarDeclS s1 "testMessage";
      |VarDeclS s2 "test2";
      |VarDeclS s3 "test3";
      |Print "stringMessage";
      |Print s1;
      |Print s2;
      |HeapAnalyze;
      |Collect;
      |HeapAnalyze;""".stripMargin
  )(
    new TreeVarDecl(new Identifier("cycle")),
    new RefAssignment(new Identifier("cycle.f1"), new Identifier("cycle")),
    new IntConstAssignment(new Identifier("cycle.data"), 23),
    new StringVarDecl(new Identifier("s1"), "testMessage"),
    new StringVarDecl(new Identifier("s2"), "test2"),
    new StringVarDecl(new Identifier("s3"), "test3"),
    new PrintConst("stringMessage"),
    new PrintRef(new Identifier("s1")),
    new PrintRef(new Identifier("s2")),
    new HeapAnalyze,
    new Collect,
    new HeapAnalyze
  )
}

