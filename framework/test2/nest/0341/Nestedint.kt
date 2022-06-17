import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {
    fun flatNested(niList: List<NestedInteger>): List<Int> {
        val res = mutableListOf<Int>()
        val iter = NestedIterator(niList)
        while (iter.hasNext()) {
            res += iter.next()
        }
        return res
    }
}

class NestedIterator(nestedList: List<NestedInteger>) {

    class Node(private val list: List<NestedInteger>) {
        var index: Int = 0
        fun isEnd(): Boolean = index >= list.size
        fun value(): Int = current().getInteger()!!
        fun current(): NestedInteger = list[index]
    }

    private val stk = java.util.ArrayDeque<Node>()

    init {
        stk.offer(Node(nestedList))
        locate()
    }

    private fun locate() {
        while (!stk.isEmpty()) {
            if (stk.peekLast().isEnd()) {
                stk.pollLast()
                if (!stk.isEmpty()) {
                    stk.peekLast().index += 1
                }
            } else if (stk.peekLast().current().isInteger()) {
                break
            } else {
                stk.offer(Node(stk.peekLast().current().getList()!!))
            }
        }
    }

    fun next(): Int {
        val value = stk.peekLast().value()
        stk.peekLast().index += 1
        locate()
        return value
    }

    fun hasNext(): Boolean {
        return !stk.isEmpty() && !stk.peekLast().isEnd()
    }
}

class Nestedint : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::flatNested)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Nestedint()(Utils.fromStdin()))
}

