using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public void ReorderList(ListNode head) {
        var slow = head;
        var fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        if (slow == fast) {
            return;
        }

        var r = reverse(slow.next);
        slow.next = null;
        merge(head, r);
    }

    private ListNode reverse(ListNode head) {
        ListNode q = null;
        while (head != null) {
            var next = head.next;
            head.next = q;
            q = head;
            head = next;
        }
        return q;
    }

    private void merge(ListNode L1, ListNode L2) {
        var t = new ListNode();
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

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().ReorderList));
        // var work = WorkFactory.ForStruct<STRUCT>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
