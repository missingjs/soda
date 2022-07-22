using Soda.Unittest;

public class Solution {
    public IList<double> Multiply(double[] a, double[] b) {
        var res = new double[a.Length];
        for (int i = 0; i < res.Length; ++i) {
            res[i] = a[i] * b[i];
        }
        return res.ToList();
    }
}

public class Numeric
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().Multiply));
        // work.SetValidator((e, r) => ...);
        // work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
