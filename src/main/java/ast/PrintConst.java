package ast;

public class PrintConst implements Statement {
    private final String value;

    public PrintConst(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrintConst that = (PrintConst) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "PrintConst{" +
                "value='" + value + '\'' +
                '}';
    }
}
