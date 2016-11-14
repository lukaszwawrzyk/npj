package parsing.statements;

import ast.Statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstantParser extends StatementParser {
    private final Statement statement;

    public ConstantParser(String identifier, Statement statement, StatementParser fallback) {
        super(Pattern.compile(Pattern.quote(identifier)), fallback);
        this.statement = statement;
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        return statement;
    }
}
