import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    public ListNode mergeKLists(ListNode[] lists) {
        var qu = new PriorityQueue<ListNode>(
            (n1, n2) -> n1.val - n2.val
        );
        for (var L : lists) {
            if (L != null) {
                qu.offer(L);
            }
        }

        var head = new ListNode(0);
        var tail = head;
        while (!qu.isEmpty()) {
            var t = qu.poll();
            var L = t.next;
            if (L != null) {
                qu.offer(L);
            }
            tail.next = t;
            tail = t;
        }
        return head.next;
    }
}

public class Leet implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create(new Solution()::mergeKLists);
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
