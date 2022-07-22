import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class VirIndex(private val nums: IntArray) {
    private val N = nums.size

    operator fun get(index: Int): Int {
        return nums[mapIndex(index)]
    }

    operator fun set(index: Int, value: Int) {
        nums[mapIndex(index)] = value
    }

    private fun mapIndex(i: Int): Int {
        if ((N and 1) == 1 || i > ((N-1) shr 1)) {
            return (((N-i) shl 1) - 1) % N
        } else {
            return (N - 2 - (i shl 1))
        }
    }
}

class Solution {
    fun wiggleSort(nums: IntArray): Unit {
        val vi = VirIndex(nums)
        val N = nums.size
        quickSelect(vi, 0, N-1, (N-1)/2)
    }

    private fun quickSelect(vi: VirIndex, _start: Int, _end: Int, k: Int) {
        var (start, end) = listOf(_start, _end)
        while (start < end) {
            val p = partition(vi, start, end)
            if (k >= p[0] && k <= p[1]) {
                return
            }
            if (k > p[1]) {
                start = p[1] + 1
            } else {
                end = p[0] - 1
            }
        }
    }

    private fun partition(vi: VirIndex, start: Int, end: Int): IntArray {
        val mid = (start + end) / 2
        val pivot = getMedian(vi[start], vi[mid], vi[end])
        var (p, z) = listOf(start, end+1)
        var q = start
        while (q < z) {
            if (vi[q] < pivot) {
                val temp = vi[p]
                vi[p] = vi[q]
                vi[q] = temp
                ++p
                ++q
            } else if (vi[q] == pivot) {
                ++q
            } else {
                --z
                val temp = vi[z]
                vi[z] = vi[q]
                vi[q] = temp
            }
        }
        return intArrayOf(p, z-1)
    }

    private fun getMedian(a: Int, b: Int, c: Int): Int {
        if (a >= b) {
            return if (b >= c) b else min(a, c)
        } else {
            return if (a >= c) a else min(b, c)
        }
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.createVoid(Solution()::wiggleSort)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        work.validator = fun(_: IntArray, nums: IntArray): Boolean {
            for (i in 1 until nums.size) {
                if (i % 2 != 0 && nums[i] <= nums[i-1]
                    || i % 2 == 0 && nums[i] >= nums[i-1]) {
                    return false
                }
            }
            return true
        }
        work.compareSerial = true
        return work.run(text)
    }
}

fun main() {
    print(Leet()(Utils.fromStdin()))
}

