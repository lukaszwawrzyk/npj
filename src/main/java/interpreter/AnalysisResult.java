package interpreter;

import java.util.ArrayList;
import java.util.Collection;

public class AnalysisResult {
    private final Collection<String> strings;
    private final Collection<Integer> treeData;

    public AnalysisResult() {
        this.strings = new ArrayList<>();
        this.treeData = new ArrayList<>();
    }

    public Collection<String> getStrings() {
        return strings;
    }

    public Collection<Integer> getTreeData() {
        return treeData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AnalysisResult that = (AnalysisResult) o;

        if (!strings.equals(that.strings)) return false;
        return treeData.equals(that.treeData);

    }

    @Override
    public int hashCode() {
        int result = strings.hashCode();
        result = 31 * result + treeData.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AnalysisResult{" +
                "strings=" + strings +
                ", treeData=" + treeData +
                '}';
    }
}
