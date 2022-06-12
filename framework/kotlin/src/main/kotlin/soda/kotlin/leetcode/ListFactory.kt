package soda.kotlin.leetcode

object ListFactory {

    fun create(values: List<Int>): ListNode? {
        val head = ListNode(-1)
        var tail = head
        for (value in values) {
            val node = ListNode(value)
            tail.next = node
            tail = node
        }
        return head.next
    }

    fun dump(head: ListNode?): List<Int> {
        var p = head
        val list = mutableListOf<Int>()
        while (p != null) {
            list += p.`val`
            p = p.next
        }
        return list.toList()
    }

}