import interpreter.Output;

public class NPJOutput implements Output {
    @Override public void printLine(String value) {
        String toPrint = value == null ? "NULL" : value;
        NPJ.print(toPrint);
    }
}
