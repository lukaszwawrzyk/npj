package parsing.statements;

import ast.Identifier;
import ast.IntConstAssignment;
import ast.Statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntConstAssignmentParser extends StatementParser {
    public IntConstAssignmentParser(StatementParser fallback) {
        super(Pattern.compile("(" + CommonPatterns.DEREFERENCE + ") = (" + CommonPatterns.NUMBER + ")"), fallback);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        String targetReference = matcher.group(1);
        String intAsString = matcher.group(2);
        int intValue = Integer.parseInt(intAsString);
        return new IntConstAssignment(new Identifier(targetReference), intValue);
    }
}
