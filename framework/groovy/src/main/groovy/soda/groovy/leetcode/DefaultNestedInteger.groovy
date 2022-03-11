package soda.groovy.leetcode

class DefaultNestedInteger implements NestedInteger {

    private final boolean isAtomic

    private List<NestedInteger> list

    private int value

    DefaultNestedInteger() {
        isAtomic = false
        list = new ArrayList<>()
    }

    DefaultNestedInteger(int value) {
        isAtomic = true
        this.value = value
        list = Collections.emptyList()
    }

    boolean isInteger() {
        isAtomic
    }

    int getInteger() {
        value
    }

    void setInteger(int value) {
        this.value = value
    }

    void add(NestedInteger ni) {
        if (!isInteger()) {
            list << ni
        }
    }

    List<NestedInteger> getList() {
        Collections.unmodifiableList(list);
    }

    String toString() {
        isInteger() ? String.valueOf(value) : list.toString()
    }
}

