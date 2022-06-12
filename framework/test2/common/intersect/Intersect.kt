import soda.kotlin.unittest.*

class Solution {
    fun intersection(nums1: IntArray, nums2: IntArray): IntArray {
        if (nums1.size > nums2.size) {
            return intersection(nums2, nums1)
        }

        val mset = mutableSetOf<Int>()
        val res = mutableSetOf<Int>()
        for (n in nums1) {
            mset += n
        }
        for (b in nums2) {
            if (b in mset) {
                res += b
            }
        }
        return res.toIntArray()
    }
}

class Intersect : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::intersection)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        work.validator = Validators.forIntArray(false)
        // work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Intersect()(Utils.fromStdin()))
}
