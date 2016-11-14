package parsing.statements;

public class CommonPatterns {
    public static final String IDENTIFIER = "[a-z][a-z0-9]*";
    public static final String DEREFERENCE = IDENTIFIER + "(?:\\." + IDENTIFIER + ")*";
    public static final String NULL = "NULL";
    public static final String NUMBER = "[0-9]+";
}
