using Soda.Unittest;
using Soda.Leetcode;

public class NumMatrix
{
    private int[][] matrix;

    private int[][] bit;

    private int rows, cols;

    public NumMatrix(int[][] matrix)
    {
        this.matrix = matrix;
        rows = matrix.Length + 1;
        cols = matrix[0].Length + 1;
        bit = new int[rows][];
        for (int i = 0; i < rows; ++i) {
            bit[i] = new int[cols];
        }
        for (int i = 0; i < rows-1; ++i) {
            for (int j = 0; j < cols-1; ++j) {
                int val = matrix[i][j];
                matrix[i][j] = 0;
                Update(i, j, val);
            }
        }
    }

    public void Update(int row, int col, int val)
    {
        int diff = val - matrix[row][col];
        matrix[row][col] = val;
        for (int i = row+1; i < this.rows; i += (i & -i)) {
            for (int j = col+1; j < this.cols; j += (j & -j)) {
                bit[i][j] += diff;
            }
        }
    }

    public int SumRegion(int row1, int col1, int row2, int col2)
    {
        return Query(row1, col1) - Query(row1, col2+1) - Query(row2+1, col1) + Query(row2+1, col2+1);
    }

    private int Query(int r, int c)
    {
        int res = 0;
        for (int i = r; i > 0; i -= (i & -i)) {
            for (int j = c; j > 0; j -= (j & -j)) {
                res += bit[i][j];
            }
        }
        return res;
    }
}

public class Query2d
{
    public static void Main(string[] args)
    {
        // var work = WorkFactory.Create(Utils.Fn(new Solution().Add));
        var work = WorkFactory.ForStruct<NumMatrix>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
