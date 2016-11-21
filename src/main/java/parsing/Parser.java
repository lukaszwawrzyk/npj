package parsing;

import ast.Collect;
import ast.HeapAnalyze;
import ast.Program;
import ast.Statement;
import parsing.statements.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final StatementParser statementParser;

    public Parser() {
        statementParser =
            new PrintConstParser(
            new PrintRefParser(
            new ConstantParser("HeapAnalyze", new HeapAnalyze(),
            new ConstantParser("Collect", new Collect(),
            new TreeVarDeclParser(
            new StringConstVarDeclParser(
            new StringNullVarDeclParser(
            new NullAssignmentParser(
            new RefAssignmentParser(
            new IntConstAssignmentParser(
            new StringConstAssignmentParser(
            new FailingParser())))))))))));
    }

    public ParseResult parse(String text) {
        try {
            List<String> statementLines = getStatementLines(text);
            List<Statement> parsedStatements = parseStatements(statementLines);

            return ParseResult.success(new Program(parsedStatements));
        } catch (ParseException e) {
            return ParseResult.failure(e.getMessage());
        }
    }

    private List<Statement> parseStatements(List<String> statementLines) {
        List<Statement> statements = new ArrayList<Statement>();
        for (String statement : statementLines) {
            Statement parsedStatement = parseStatement(statement);
            statements.add(parsedStatement);
        }
        return statements;
    }

    private Statement parseStatement(String statement) {
        return statementParser.tryParse(statement);
    }

    private List<String> getStatementLines(String text) {
        String[] split = text.split(";");
        List<String> statements = new ArrayList<String>();
        for (String line : split) {
            String trimmedLine = line.trim();
            if (!trimmedLine.isEmpty()) {
                statements.add(trimmedLine);
            }
        }
        return statements;
    }
}
