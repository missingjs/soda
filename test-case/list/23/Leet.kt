import soda.kotlin.leetcode.*
import soda.kotlin.unittest.*

import java.util.PriorityQueue

class Solution {
    fun mergeKLists(lists: Array<ListNode?>): ListNode? {
        val qu = PriorityQueue<ListNode> { n1, n2 -> n1.`val` - n2.`val` }
        for (L in lists) {
            if (L != null) {
                qu.offer(L)
            }
        }

        val head = ListNode(0)
        var tail = head
        while (!qu.isEmpty()) {
            val t = qu.poll()
            val L = t.next
            if (L != null) {
                qu.offer(L)
            }
            tail.next = t
            tail = t
        }
        return head.next
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::mergeKLists)

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
