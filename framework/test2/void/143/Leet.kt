import kotlin.math.*
import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

class Solution {
    fun reorderList(head: ListNode?): Unit {
        var slow = head
        var fast = head
        while (fast!!.next != null && fast.next!!.next != null) {
            slow = slow!!.next
            fast = fast.next!!.next
        }

        if (slow == fast) {
            return
        }

        val r = reverse(slow!!.next)
        slow.next = null
        merge(head, r)
    }

    private fun reverse(head: ListNode?): ListNode? {
        var q: ListNode? = null
        var H = head
        while (H != null) {
            val next = H.next
            H.next = q
            q = H 
            H = next
        }
        return q
    }

    private fun merge(L1: ListNode?, L2: ListNode?) {
        var t = ListNode(0)
        var (h1, h2) = listOf(L1, L2)
        while (h1 != null && h2 != null) {
            t.next = h1
            t = h1
            h1 = h1.next
            t.next = h2
            t = h2
            h2 = h2.next
        }
        t.next = if (h1 != null) h1 else h2
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.createVoid(Solution()::reorderList)

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

