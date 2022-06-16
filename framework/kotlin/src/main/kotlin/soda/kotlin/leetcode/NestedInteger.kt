package soda.kotlin.leetcode

// This is the interface that allows for creating nested lists.
// You should not implement it, or speculate about its implementation
class NestedInteger {
    // Constructor initializes an empty nested list.
    constructor() {
        this.isAtomic = false
        this.list = mutableListOf()
    }

    // Constructor initializes a single integer.
    constructor(value: Int) {
        this.isAtomic = true
        this.value = value
    }

    private var list: MutableList<NestedInteger>? = null

    private var value: Int? = null

    private val isAtomic: Boolean

    // @return true if this NestedInteger holds a single integer, rather than a nested list.
    fun isInteger(): Boolean = isAtomic

    // @return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    fun getInteger(): Int? = value

    // Set this NestedInteger to hold a single integer.
    fun setInteger(value: Int): Unit {
        this.value = value
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer to it.
    fun add(ni: NestedInteger): Unit {
        list?.add(ni)
    }

    // @return the nested list that this NestedInteger holds, if it holds a nested list
    // Return null if this NestedInteger holds a single integer
    fun getList(): List<NestedInteger>? = list?.toList()
}