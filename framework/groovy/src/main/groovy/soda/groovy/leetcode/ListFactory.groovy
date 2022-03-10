package soda.groovy.leetcode

class ListFactory {

    static ListNode create(List<Integer> values) {
        def head = new ListNode()
        def tail = head
        for (value in values) {
            def node = new ListNode(value)
            tail.next = node
            tail = node
        }
        head.next
    }

    static List<Integer> dump(ListNode head) {
        def list = []
        while (head != null) {
            list << head.val
            head = head.next
        }
        list
    }

}
