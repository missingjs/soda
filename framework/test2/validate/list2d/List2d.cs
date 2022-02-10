using Soda.Unittest;
using Soda.Leetcode;

public class Solution {
    public string[][] GroupByLength(string[] strs)
    {
        var rand = new Random();
        var strList = new List<string>(strs).OrderBy(x => rand.Next()).ToList();
        var group = new Dictionary<int, IList<string>>();
        foreach (var s in strList) {
            IList<string> L = null;
            if (!group.TryGetValue(s.Length, out L)) {
                group[s.Length] = L = new List<string>();
            }
            L.Add(s);
        }
        var keys = new List<int>(group.Keys);
        keys = keys.OrderBy(x => rand.Next()).ToList();
        var res = new string[group.Count][];
        for (int i = 0; i < res.Length; ++i) {
            res[i] = group[keys[i]].ToArray();
        }
        return res;
    }
}

public class List2d
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().GroupByLength));
        // var work = WorkFactory.ForStruct<STRUCT>();
        work.SetValidator(Validators.ForArray2d<string>(false, false));
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
