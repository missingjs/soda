using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public ListNode MergeKLists(ListNode[] lists) {
        var qu = new PriorityQueue<ListNode, int>();
        foreach (var L in lists) {
            if (L != null) {
                qu.Enqueue(L, L.val);
            }
        }

        var head = new ListNode(0);
        var tail = head;
        while (qu.Count > 0) {
            var t = qu.Dequeue();
            var L = t.next;
            if (L != null) {
                qu.Enqueue(L, L.val);
            }
            tail.next = t;
            tail = t;
        }
        return head.next;
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().MergeKLists));
        // var work = WorkFactory.ForStruct<STRUCT>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
