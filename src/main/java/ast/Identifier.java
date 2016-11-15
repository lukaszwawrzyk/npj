package ast;

import java.util.Arrays;
import java.util.List;

public class Identifier {
    private final String value;
    private List<String> components;

    public Identifier(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Identifier that = (Identifier) o;

        return value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "Identifier{" +
                "value='" + value + '\'' +
                '}';
    }

    public String firstComponent() {
        return getComponents().get(0);
    }

    public String lastComponent() {
        return getComponents().get(getComponents().size() - 1);
    }

    public List<String> intermediateComponents() {
        return getComponents().subList(1, getComponents().size() - 1);
    }

    public List<String> nestedComponents() {
        return getComponents().subList(1, getComponents().size());
    }

    public List<String> getComponents() {
        if (components == null) {
            components = Arrays.asList(value.split("\\."));
        }
        return components;
    }

    public boolean hasSingleComponent() {
        return getComponents().size() == 1;
    }
}
