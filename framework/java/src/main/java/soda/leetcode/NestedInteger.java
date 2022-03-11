package soda.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NestedInteger {

    private final boolean isAtomic;

    private List<NestedInteger> list;

    private int value;

    // Constructor initializes an empty nested list.
    public NestedInteger() {
        isAtomic = false;
        list = new ArrayList<>();
    }

    // Constructor initializes a single integer.
    public NestedInteger(int value) {
        isAtomic = true;
        this.value = value;
        list = Collections.emptyList();
    }

    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    public boolean isInteger() {
        return isAtomic;
    }

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    public Integer getInteger() {
        return value;
    }

    // Set this NestedInteger to hold a single integer.
    public void setInteger(int value) {
        this.value = value;
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    public void add(NestedInteger ni) {
        list.add(ni);
    }

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return empty list if this NestedInteger holds a single integer
    public List<NestedInteger> getList() {
        return Collections.unmodifiableList(list);
    }

    @Override
    public String toString() {
        if (isInteger()) {
            return String.valueOf(value);
        }
        return list.toString();
    }
}
