using Soda.Unittest;

public class Solution {
    public List<int> Intersection(int[] nums1, int[] nums2) {
        if (nums1.Length > nums2.Length) {
            return Intersection(nums2, nums1);
        }
        var mset = new HashSet<int>();
        var res = new HashSet<int>();
        foreach (int n in nums1) {
            mset.Add(n);
        }
        foreach (int b in nums2) {
            if (mset.Contains(b)) {
                res.Add(b);
            }
        }
        return res.ToList();
    }
}

public class Intersect
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().Intersection));
        work.SetValidator(Validators.ForList<int>(false));
        // work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
