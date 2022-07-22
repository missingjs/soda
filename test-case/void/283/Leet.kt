import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {
    fun moveZeroes(nums: IntArray): Unit {
        var p = 0
        for (i in 0 until nums.size) {
            if (nums[i] != 0) {
                if (i != p) {
                    val temp = nums[p]
                    nums[p] = nums[i]
                    nums[i] = temp
                }
                ++p
            }
        }
        while (p < nums.size) {
            nums[p] = 0
            ++p
        }
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.createVoid(Solution()::moveZeroes)

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

