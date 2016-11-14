package parsing.statements;

import ast.Identifier;
import ast.Statement;
import ast.RefAssignment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RefAssignmentParser extends StatementParser {
    public RefAssignmentParser(StatementParser fallback) {
        super(Pattern.compile("(" + CommonPatterns.DEREFERENCE + ")" + " = " + "(" + CommonPatterns.DEREFERENCE + ")"), fallback);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        String destinationRef = matcher.group(1);
        String sourceRef = matcher.group(2);
        return new RefAssignment(new Identifier(destinationRef), new Identifier(sourceRef));
    }
}
