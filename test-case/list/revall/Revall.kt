import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {
    fun reverseAll(lists: List<ListNode?>): Array<ListNode?> {
        val res = mutableListOf<ListNode?>()
        for (elem in lists) {
            res += reverse(elem)
        }
        var i = 0
        var j = res.size - 1
        while (i < j) {
            val temp = res[i]
            res[i] = res[j]
            res[j] = temp
            ++i
            --j
        }
        return res.toTypedArray()
    }

    private fun reverse(hd: ListNode?): ListNode? {
        var h: ListNode? = null
        var head = hd
        while (head != null) {
            val next = head.next
            head.next = h
            h = head
            head = next
        }
        return h
    }
}

class Revall : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::reverseAll)

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
    print(Revall()(Utils.fromStdin()))
}
