import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    public List<ListNode> reverseAll(List<ListNode> lists) {
        def res = []
        for (int i = 0; i < lists.size(); ++i) {
            res << reverse(lists[i])
        }
        int i = 0, j = res.size() - 1
        while (i < j) {
            def temp = res[i]
            res[i] = res[j]
            res[j] = temp
            ++i
            --j
        }
        res
    }

    private ListNode reverse(ListNode head) {
        ListNode h = null
        while (head) {
            def next = head.next
            head.next = h
            h = head
            head = next
        }
        h
    }
}

def work = TestWork.create(new Solution().&reverseAll)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
