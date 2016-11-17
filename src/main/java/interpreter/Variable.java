package interpreter;

public class Variable {
    private final String name;
    private final int pointer;

    public Variable(String name, int pointer) {
        this.name = name;
        this.pointer = pointer;
    }

    public String getName() {
        return name;
    }

    public int getPointer() {
        return pointer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable variable = (Variable) o;

        return pointer == variable.pointer && name.equals(variable.name);

    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + pointer;
        return result;
    }

    @Override
    public String toString() {
        return "Variable{" +
                "name='" + name + '\'' +
                ", pointer=" + pointer +
                '}';
    }
}
