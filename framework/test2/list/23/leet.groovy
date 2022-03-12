import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        def qu = new PriorityQueue<ListNode>(
            (n1, n2) -> n1.val - n2.val
        )
        for (L in lists) {
            if (L != null) {
                qu.offer(L)
            }
        }

        def head = new ListNode(0)
        def tail = head
        while (!qu.isEmpty()) {
            def t = qu.poll()
            def L = t.next
            if (L != null) {
                qu.offer(L)
            }
            tail.next = t
            tail = t
        }
        head.next
    }
}

class LeetWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&mergeKLists)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new LeetWork()(System.in.getText('UTF-8'))
