package parsing;

import ast.Program;

public class ParseResult {
    public static ParseResult failure(String message) {
        return new ParseResult(null, message);
    }

    public static ParseResult success(Program program) {
        return new ParseResult(program, null);
    }

    private ParseResult(Program success, String error) {
        this.success = success;
        this.error = error;
    }

    private final Program success;
    private final String error;

    public boolean isSuccessful() {
        return success != null;
    }

    public Program getParsedResult() {
        return success;
    }

    public String getErrorMessage() {
        return error;
    }
}
