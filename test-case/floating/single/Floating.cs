using Soda.Unittest;

public class Solution {
    public double Divide(double a, double b) {
        return a / b;
    }
}

public class Floating
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().Divide));
        // work.SetValidator((e, r) => ...);
        // work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
