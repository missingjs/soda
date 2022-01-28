import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.TestWork;


class Solution {
    public List<ListNode> reverseAll(List<ListNode> lists) {
        var res = new ArrayList<ListNode>();
        for (int i = 0; i < lists.size(); ++i) {
            res.add(reverse(lists.get(i)));
        }
        int i = 0, j = res.size() - 1;
        while (i < j) {
            var temp = res.get(i);
            res.set(i, res.get(j));
            res.set(j, temp);
            ++i;
            --j;
        }
        return res;
    }

    private ListNode reverse(ListNode head) {
        ListNode h = null;
        while (head != null) {
            var next = head.next;
            head.next = h;
            h = head;
            head = next;
        }
        return h;
    }
}

public class Revall implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "reverseAll");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Revall().get().run();
    }

}
