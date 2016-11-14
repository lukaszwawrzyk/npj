package parsing.statements;

import ast.Identifier;
import ast.Statement;
import ast.StringConstAssignment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringConstAssignmentParser extends StatementParser {
    public StringConstAssignmentParser(StatementParser fallback) {
        super(Pattern.compile("(" + CommonPatterns.DEREFERENCE + ") = \"" + "(.*)" + "\""), fallback);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        String targetReference = matcher.group(1);
        String stringValue = matcher.group(2);
        return new StringConstAssignment(new Identifier(targetReference), stringValue);
    }
}
