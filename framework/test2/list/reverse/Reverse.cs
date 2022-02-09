using Soda.Unittest;
using Soda.Leetcode;


public class Solution {
    public ListNode Reverse(ListNode head)
    {
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

public class Reverse
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().Reverse));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
