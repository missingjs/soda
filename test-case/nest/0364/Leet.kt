import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

data class Info(var sum: Int, var product: Int, var maxDepth: Int)

class Solution {
    fun depthSumInverse(nestedList: List<NestedInteger>): Int {
        val info = getInfo(nestedList, 1)
        return (info.maxDepth + 1) * info.sum - info.product
    }

    private fun getInfo(nestedList: List<NestedInteger>, depth: Int): Info {
        var sum = 0
        var product = 0
        var maxDepth = depth
        for (ni in nestedList) {
            if (ni.isInteger()) {
                val value = ni.getInteger()!!
                sum += value
                product += value * depth
                maxDepth = max(maxDepth, depth)
            } else {
                val res = getInfo(ni.getList()!!, depth + 1)
                sum += res.sum
                product += res.product
                maxDepth = max(maxDepth, res.maxDepth)
            }
        }
        return Info(sum, product, maxDepth)
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::depthSumInverse)

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
    print(Leet()(Utils.fromStdin()))
}
