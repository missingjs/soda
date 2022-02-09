using Soda.Leetcode;

namespace Soda.Unittest;

public class __Solution__ {
    public int Add(int a, int b) {
        return a + b;
    }
}

public class __Bootstrap__
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new __Solution__().Add));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
