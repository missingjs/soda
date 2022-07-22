using Soda.Unittest;
using Soda.Leetcode;


public class Solution {
    class Info {
        public int Sum, Product, MaxDepth;
        public Info(int s, int p, int m) {
            Sum = s;
            Product = p;
            MaxDepth = m;
        }
    }

    public int DepthSumInverse(IList<NestedInteger> nestedList)
    {
        var info = getInfo(nestedList, 1);
        return (info.MaxDepth + 1) * info.Sum - info.Product;
    }

    private Info getInfo(IList<NestedInteger> nestedList, int depth) {
        int sum = 0, product = 0, maxDepth = depth;
        foreach (var ni in nestedList) {
            if (ni.IsInteger()) {
                int val = ni.GetInteger();
                sum += val;
                product += val * depth;
                maxDepth = Math.Max(maxDepth, depth);
            } else {
                var res = getInfo(ni.GetList(), depth + 1);
                sum += res.Sum;
                product += res.Product;
                maxDepth = Math.Max(maxDepth, res.MaxDepth);
            }
        }
        return new Info(sum, product, maxDepth);
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().DepthSumInverse));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
