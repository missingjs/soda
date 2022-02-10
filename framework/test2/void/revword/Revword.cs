using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public void ReverseWords(char[] s)
    {
        if (s.Length == 0) {
            return;
        }

        reverse(s, 0, s.Length - 1);

        int N = s.Length;
        int i = 0, j = 0;
        while (j < N) {
            if (s[j] == ' ') {
                reverse(s, i, j-1);
                i = j + 1;
            }
            ++j;
        }
        if (i < j) {
            reverse(s, i, j-1);
        }
    }

    private void reverse(char[] s, int i, int j) {
        while (i < j) {
            (s[i], s[j]) = (s[j], s[i]);
            ++i;
            --j;
        }
    }
}

public class Revword
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().ReverseWords));
        // var work = WorkFactory.ForStruct<STRUCT>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
