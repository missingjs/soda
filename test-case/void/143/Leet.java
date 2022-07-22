import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    public void reorderList(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        if (slow == fast) {
            return;
        }

        ListNode r = reverse(slow.next);
        slow.next = null;
        merge(head, r);
    }

    private ListNode reverse(ListNode head) {
        ListNode q = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = q;
            q = head;
            head = next;
        }
        return q;
    }

    private void merge(ListNode L1, ListNode L2) {
        ListNode h = new ListNode(), t = h;
        while (L1 != null && L2 != null) {
            t.next = L1;
            t = L1;
            L1 = L1.next;
            t.next = L2;
            t = L2;
            L2 = L2.next;
        }
        t.next = L1 != null ? L1 : L2;
    }
}

public class Leet implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create(new Solution()::reorderList);
        // var work = GenericTestWork.forStruct(Solution.class);
        // var work = TestWork.forObject(new Solution(), "METHOD");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work.run(text);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Leet().apply(Utils.fromStdin()));
    }

}
