using Soda.Unittest;
using Soda.Leetcode;

public class SummaryRanges {

    private int[] parent;

    private ISet<int> ancestorSet = new HashSet<int>();

    public SummaryRanges() {
        parent = new int[10003];
    }
    
    public void AddNum(int val) {
        ++val;
        
        if (parent[val] != 0) {
            return;
        }

        parent[val] = -1;
        ancestorSet.Add(val);

        int left = val - 1, right = val + 1;
        if (left > 0 && parent[left] != 0) {
            merge(left, val);
        }
        if (parent[right] != 0) {
            merge(val, right);
        }
    }
    
    public int[][] GetIntervals() {
        var ans = new List<int>(ancestorSet);
        ans.Sort();
        var res = new int[ans.Count][];
        for (int i = 0; i < res.Length; ++i) {
            int start = ans[i];
            int end = start - parent[start] - 1;
            res[i] = new int[] { start-1, end-1 };
        }
        return res;
    }

    private void merge(int x, int y) {
        int ax = getAncestor(x), ay = getAncestor(y);
        if (ax < ay) {
            mergeAncestor(ax, ay);
        } else {
            mergeAncestor(ay, ax);
        }
    }

    private void mergeAncestor(int ax, int ay) {
        parent[ax] += parent[ay];
        parent[ay] = ax;
        ancestorSet.Remove(ay);
    }

    private int getAncestor(int x) {
        return parent[x] < 0 ? x : (parent[x] = getAncestor(parent[x]));
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        // var work = WorkFactory.Create(Utils.Fn(new Solution().Add));
        var work = WorkFactory.ForStruct<SummaryRanges>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
