using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public string[] Permutation(char[] chars, int n)
    {
        var res = new System.Collections.Generic.List<string>();
        var buf = new char[n];
        solve(chars, 0, buf, 0, res);
        return res.ToArray();
    }

    private void solve(char[] chars, int i, char[] buf, int j, IList<string> res)
    {
        if (j == buf.Length) {
            res.Add(new string(buf));
            return;
        }
        for (int k = i; k < chars.Length; ++k) {
            (chars[i], chars[k]) = (chars[k], chars[i]);
            buf[j] = chars[i];
            solve(chars, i+1, buf, j+1, res);
            (chars[i], chars[k]) = (chars[k], chars[i]);
        }
    }
}

public class List
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().Permutation));
        // var work = WorkFactory.ForStruct<STRUCT>();
        work.SetValidator(Validators.ForArray<string>(false));
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
