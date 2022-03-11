package soda.groovy.leetcode

interface NestedInteger {
    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    boolean isInteger()

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    int getInteger()

    // Set this NestedInteger to hold a single integer.
    void setInteger(int value)

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    void add(NestedInteger ni)

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return empty list if this NestedInteger holds a single integer
    List<NestedInteger> getList()
}

