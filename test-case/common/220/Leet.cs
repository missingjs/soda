using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public bool ContainsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        var map = new Dictionary<long, int>();
        var sset = new SortedSet<long>();
        int i = 0, j = 0;
        while (j < nums.Length) {
            if (j - i <= k) {
                long val = nums[j++];
                long lower = val - t, upper = val + t;
                var view = sset.GetViewBetween(lower, long.MaxValue);
                if (view.Count > 0 && view.Min <= upper) {
                    return true;
                }
                int c = 0;
                map.TryGetValue(val, out c);
                map[val] = c + 1;
                sset.Add(val);
            } else {
                long val = nums[i++];
                int count = map[val] - 1;
                if (count > 0) {
                    map[val] = count;
                } else {
                    map.Remove(val);
                    sset.Remove(val);
                }
            }
        }
        return false;
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().ContainsNearbyAlmostDuplicate));
        // var work = WorkFactory.ForStruct<STRUCT>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
