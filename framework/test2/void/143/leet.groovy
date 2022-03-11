import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    void reorderList(ListNode head) {
        ListNode slow = head, fast = head
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next
            fast = fast.next.next
        }
        if (slow == fast) {
            return
        }

        ListNode r = reverse(slow.next)
        slow.next = null
        merge(head, r)
    }

    private ListNode reverse(ListNode head) {
        ListNode q = null
        while (head != null) {
            ListNode next = head.next
            head.next = q
            q = head
            head = next
        }
        q
    }

    private void merge(ListNode L1, ListNode L2) {
        ListNode h = new ListNode(), t = h
        while (L1 != null && L2 != null) {
            t.next = L1
            t = L1
            L1 = L1.next
            t.next = L2
            t = L2
            L2 = L2.next
        }
        t.next = L1 != null ? L1 : L2;
    }
}

def work = TestWork.create(new Solution().&reorderList)
// def work = TestWork.forStruct(STRUCT)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
