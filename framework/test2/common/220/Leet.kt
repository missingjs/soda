import java.util.TreeMap
import soda.kotlin.unittest.*

class Solution {
    fun containsNearbyAlmostDuplicate(nums: IntArray, k: Int, t: Int): Boolean {
        val imap = java.util.TreeMap<Long, Int>()
        var i = 0
        var j = 0
        while (j < nums.size) {
            if (j - i <= k) {
                val value = nums[j++].toLong()
                val lower = value - t
                val upper = value + t
                imap.ceilingKey(lower)?.let {
                    if (it <= upper) {
                        return true
                    }
                }
                imap[value] = imap.getOrDefault(value, 0) + 1
            } else {
                val value = nums[i++].toLong()
                val c = imap.getOrDefault(value, 0) - 1
                if (c == 0) {
                    imap.remove(value)
                } else {
                    imap[value] = c
                }
            }
        }
        return false
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        val work = GenericTestWork.create(Solution()::containsNearbyAlmostDuplicate)

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
