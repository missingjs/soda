using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public void MoveZeroes(int[] nums) {
        int p = 0;
        for (int i = 0; i < nums.Length; ++i) {
            if (nums[i] != 0) {
                if (i != p) {
                    (nums[p], nums[i]) = (nums[i], nums[p]);
                }
                ++p;
            }
        }
        while (p < nums.Length) {
            nums[p] = 0;
            ++p;
        }
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().MoveZeroes));
        // var work = WorkFactory.ForStruct<STRUCT>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
