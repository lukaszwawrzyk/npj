package parsing.statements;

import ast.Identifier;
import ast.PrintRef;
import ast.Statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintRefParser extends StatementParser {
    public PrintRefParser(StatementParser fallback) {
        super(Pattern.compile("Print ("+ CommonPatterns.IDENTIFIER +")"), fallback);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        String identifier = matcher.group(1);
        return new PrintRef(new Identifier(identifier));
    }
}