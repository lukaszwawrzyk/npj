package parsing.statements;

import ast.Statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StatementParser {
    private final Pattern pattern;
    private final StatementParser fallback;

    protected StatementParser(Pattern pattern, StatementParser fallback) {
        this.pattern = pattern;
        this.fallback = fallback;
    }

    public Statement tryParse(String statement) {
        Matcher matcher = pattern.matcher(statement);
        if (matcher.matches()) {
            return createStatement(matcher);
        } else {
            return fallback.tryParse(statement);
        }
    }

    protected abstract Statement createStatement(Matcher matcher);
}
