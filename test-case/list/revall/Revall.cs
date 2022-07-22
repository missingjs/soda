using Soda.Unittest;
using Soda.Leetcode;


public class Solution {
    public IList<ListNode> ReverseAll(IList<ListNode> lists)
    {
        var res = new List<ListNode>();
        for (int i = 0; i < lists.Count; ++i) {
            res.Add(reverse(lists[i]));
        }
        int I = 0, J = res.Count - 1;
        while (I < J) {
            var temp = res[I];
            res[I] = res[J];
            res[J] = temp;
            ++I;
            --J;
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

public class Revall
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().ReverseAll));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
