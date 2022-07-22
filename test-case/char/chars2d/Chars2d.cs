using Soda.Unittest;

public class Solution {
    public char[][] ToUpper(IList<IList<char>> matrix)
    {
        int diff = 'a' - 'A';
        int cols = matrix[0].Count;
        char[][] mx = new char[matrix.Count][];
        for (int i = 0; i < matrix.Count; ++i) {
            mx[i] = new char[cols];
            for (int j = 0; j < cols; ++j) {
                mx[i][j] = (char) (matrix[i][j] - diff);
            }
        }
        return mx;
    }
}

public class Chars2d
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().ToUpper));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
