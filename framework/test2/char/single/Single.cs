using Soda.Unittest;

public class Solution {
    public char NextChar(char ch) {
        return (char) (ch + 1);
    }
}

public class Single
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().NextChar));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
