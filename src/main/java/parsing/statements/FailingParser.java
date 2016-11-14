package parsing.statements;

import ast.Statement;
import parsing.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FailingParser extends StatementParser {
    public FailingParser() {
        super(Pattern.compile(".*"), null);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        throw new ParseException(":(");
    }
}
