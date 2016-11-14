package parsing.statements;

import ast.Identifier;
import ast.Statement;
import ast.StringVarDecl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringConstVarDeclParser extends StatementParser {
    public StringConstVarDeclParser(StatementParser fallback) {
        super(Pattern.compile("VarDeclS (" + CommonPatterns.IDENTIFIER + ") \"(.*)\""), fallback);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        String identifier = matcher.group(1);
        String constantValue = matcher.group(2);
        return new StringVarDecl(new Identifier(identifier), constantValue);
    }
}
