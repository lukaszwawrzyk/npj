package parsing.statements;

import ast.Identifier;
import ast.Statement;
import ast.StringVarDecl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringNullVarDeclParser extends StatementParser {
    public StringNullVarDeclParser(StatementParser fallback) {
        super(Pattern.compile("VarDeclS (" + CommonPatterns.IDENTIFIER + ") " + CommonPatterns.NULL), fallback);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        String identifier = matcher.group(1);
        return new StringVarDecl(new Identifier(identifier), null);
    }

}
