package soda.groovy.leetcode

class NestedInteger {

    private final boolean isAtomic

    private List<NestedInteger> list

    private int value

    NestedInteger() {
        isAtomic = false
        list = new ArrayList<>()
    }

    NestedInteger(int value) {
        isAtomic = true
        this.value = value
        list = Collections.emptyList()
    }

    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    boolean isInteger() {
        isAtomic
    }

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    int getInteger() {
        value
    }

    // Set this NestedInteger to hold a single integer.
    void setInteger(int value) {
        this.value = value
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    void add(NestedInteger ni) {
        if (!isInteger()) {
            list << ni
        }
    }

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return empty list if this NestedInteger holds a single integer
    List<NestedInteger> getList() {
        Collections.unmodifiableList(list);
    }

    @Override
    String toString() {
        isInteger() ? String.valueOf(value) : list.toString()
    }
}

