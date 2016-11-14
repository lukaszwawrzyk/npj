package parsing.statements;

import ast.Identifier;
import ast.Statement;
import ast.TreeVarDecl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeVarDeclParser extends StatementParser {
    public TreeVarDeclParser(StatementParser fallback) {
        super(Pattern.compile("VarDeclT (" + CommonPatterns.IDENTIFIER + ")"), fallback);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        String identifier = matcher.group(1);
        return new TreeVarDecl(new Identifier(identifier));
    }
}
