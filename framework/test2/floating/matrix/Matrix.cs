using Soda.Unittest;

public class Solution {
    public double[][] MatrixMultiply(double[][] a, IList<IList<double>> b)
    {
        int rows = a.Length, cols = b[0].Count;
        var res = new double[rows][];
        for (int i = 0; i < rows; ++i) {
            res[i] = new double[cols];
            for (int j = 0; j < cols; ++j) {
                double c = 0.0;
                for (int k = 0; k < b.Count; ++k) {
                    c += a[i][k] * b[k][j];
                }
                res[i][j] = c;
            }
        }
        return res;
    }
}

public class Matrix
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().MatrixMultiply));
        // work.SetValidator((e, r) => ...);
        // work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
