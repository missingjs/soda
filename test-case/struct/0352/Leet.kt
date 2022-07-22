import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class SummaryRanges() {

    private val parent = IntArray(10003) {0}

    private val ancestorSet = mutableSetOf<Int>()

    fun addNum(`val`: Int) {
        val value = `val` + 1
        if (parent[value] != 0) {
            return
        }

        parent[value] = -1
        ancestorSet += value

        var left = value - 1
        val right = value + 1
        if (left > 0 && parent[left] != 0) {
            merge(left, value)
        }
        if (parent[right] != 0) {
            merge(value, right)
        }
    }

    fun getIntervals(): Array<IntArray> {
        val ans = ancestorSet.toIntArray().sortedArray()
        val emptyIntArray = IntArray(0) {0}
        val res = Array<IntArray>(ans.size) {emptyIntArray}
        for (i in res.indices) {
            val start = ans[i]
            val end = start - parent[start] - 1
            res[i] = intArrayOf(start-1, end-1)
        }
        return res
    }

    fun merge(x: Int, y: Int) {
        val ax = getAncestor(x)
        val ay = getAncestor(y)
        if (ax < ay) {
            mergeAncestor(ax, ay)
        } else {
            mergeAncestor(ay, ax)
        }
    }

    fun mergeAncestor(ax: Int, ay: Int) {
        parent[ax] += parent[ay]
        parent[ay] = ax
        ancestorSet -= ay
    }

    fun getAncestor(x: Int): Int {
        if (parent[x] < 0) {
            return x
        } else {
            parent[x] = getAncestor(parent[x])
            return parent[x]
        }
    }

}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        // val work = GenericTestWork.create(Solution()::add)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        val work = GenericTestWork.forStruct(SummaryRanges::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Leet()(Utils.fromStdin()))
}

