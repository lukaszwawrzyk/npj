package parsing.statements;

import ast.Identifier;
import ast.Statement;
import ast.NullAssignment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NullAssignmentParser extends StatementParser {
    public NullAssignmentParser(StatementParser fallback) {
        super(Pattern.compile("(" + CommonPatterns.DEREFERENCE + ")" + " = " + CommonPatterns.NULL), fallback);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        String identifier = matcher.group(1);
        return new NullAssignment(new Identifier(identifier));
    }
}
