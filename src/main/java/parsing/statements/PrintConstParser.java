package parsing.statements;

import ast.PrintConst;
import ast.Statement;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrintConstParser extends StatementParser {
    public PrintConstParser(StatementParser fallback) {
        super(Pattern.compile("Print \"(.*)\""), fallback);
    }

    @Override
    protected Statement createStatement(Matcher matcher) {
        String constantValue = matcher.group(1);
        return new PrintConst(constantValue);
    }
}
