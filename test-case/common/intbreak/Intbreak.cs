using Soda.Unittest;

public class Solution {
    private int[] memo;

    public int IntegerBreak(int n) {
        memo = new int[59];
        return solve(n);
    }

    private int solve(int n) {
        if (n == 1) {
            return 1;
        }
        if (memo[n] > 0) {
            return memo[n];
        }
        int res = 0;
        for (int i = 1; i < n; ++i) {
            res = Math.Max(i * (n-i), res);
            res = Math.Max(i * solve(n-i), res);
        }
        memo[n] = res;
        return res;
    }
}

public class Intbreak
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().IntegerBreak));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
