using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public void ReverseString(char[] s)
    {
        int i = 0, j = s.Length - 1;
        while (i < j) {
            (s[i], s[j]) = (s[j], s[i]);
            ++i;
            --j;
        }
    }
}

public class Revstr
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().ReverseString));
        // var work = WorkFactory.ForStruct<STRUCT>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
