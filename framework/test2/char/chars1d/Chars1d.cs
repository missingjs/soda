using Soda.Unittest;

public class Solution {
    public IList<char> DoubleList(char[] chars)
    {
        var res = new List<char>();
        foreach (char ch in chars) {
            res.Add(ch);
        }
        foreach (char ch in chars) {
            res.Add(ch);
        }
        return res;
    }
}

public class Chars1d
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().DoubleList));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
