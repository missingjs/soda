package soda.leetcode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultNestedInteger implements NestedInteger {

    private final boolean isAtomic;

    private List<NestedInteger> list;

    private int value;

    public DefaultNestedInteger() {
        isAtomic = false;
        list = new ArrayList<>();
    }

    public DefaultNestedInteger(int value) {
        isAtomic = true;
        this.value = value;
        list = Collections.emptyList();
    }

    @Override
    public boolean isInteger() {
        return isAtomic;
    }

    @Override
    public Integer getInteger() {
        return value;
    }

    @Override
    public void setInteger(int value) {
        this.value = value;
    }

    @Override
    public void add(NestedInteger ni) {
        list.add(ni);
    }

    @Override
    public List<NestedInteger> getList() {
        return Collections.unmodifiableList(list);
    }
}
