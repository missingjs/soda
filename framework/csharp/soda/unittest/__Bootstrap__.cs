using soda.unittest;

public class Solution {
    public int Add(int a, int b) {
        return a + b;
    }
}

public class __Bootstrap__
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().Add));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
