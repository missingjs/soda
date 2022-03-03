using Soda.Unittest;
using Soda.Leetcode;

class VirIndex {
    private int[] nums;

    public VirIndex(int[] nums) {
        this.nums = nums;
    }

    public int this[int index] {
        set { this.nums[mapIndex(index)] = value; }
        get { return this.nums[mapIndex(index)]; }
    }

    private int mapIndex(int i) {
        var n = nums.Length;
        if ((n&1) == 1 || i > ((n-1)>>1)) {
            return (((n-i) << 1) - 1) % n;
        } else {
            return (n - 2 - (i << 1));
        }
    }
}

public class Solution {
    public void WiggleSort(int[] nums) {
        var vi = new VirIndex(nums);
        var n = nums.Length;
        quickSelect(vi, 0, n-1, (n-1)/2);
    }

    private void quickSelect(VirIndex vi, int start, int end, int k) {
        while (start < end) {
            var p = partition(vi, start, end);
            if (k >= p[0] && k <= p[1]) {
                return;
            }
            if (k > p[1]) {
                start = p[1] + 1;
            } else {
                end = p[0] - 1;
            }
        }
    }

    private int[] partition(VirIndex vi, int start, int end) {
        var mid = (start + end) / 2;
        var pivot = getMedian(vi[start], vi[mid], vi[end]);
        int p = start, z = end + 1, q = start;
        while (q < z) {
            if (vi[q] < pivot) {
                (vi[p], vi[q]) = (vi[q], vi[p]);
                ++p;
                ++q;
            } else if (vi[q] == pivot) {
                ++q;
            } else {
                --z;
                (vi[z], vi[q]) = (vi[q], vi[z]);
            }
        }
        return new int[] { p, z-1 };
    }

    private int getMedian(int a, int b, int c) {
        if (a >= b) {
            return b >= c ? b : Math.Min(a, c);
        } else {
            return a >= c ? a : Math.Min(b, c);
        }
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().WiggleSort));
        // var work = WorkFactory.ForStruct<STRUCT>();
        var validator = (int[] e, int[] nums) => {
            for (int i = 1; i < nums.Length; ++i) {
                if (i % 2 != 0 && nums[i] <= nums[i-1] || i % 2 == 0 && nums[i] >= nums[i-1]) {
                    return false;
                }
            }
            return true;
        };
        work.SetValidator(validator);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
