using Soda.Unittest;
using Soda.Leetcode;
using Newtonsoft.Json.Linq;

public class Solution {

    private int[] original;

    public Solution(int[] nums) {
        original = nums;
    }
    
    public int[] Reset() {
        var res = new int[original.Length];
        Array.Copy(original, res, original.Length);
        return res;
    }
    
    public int[] Shuffle() {
        var res = Reset();
        var rand = new Random();
        for (int s = res.Length; s > 0; --s) {
            int i = rand.Next(s);
            int j = s - 1;
            if (i != j) {
                int temp = res[i];
                res[i] = res[j];
                res[j] = temp;
            }
        }
        return res;
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        // var work = WorkFactory.Create(Utils.Fn(new Solution().Add));
        var work = WorkFactory.ForStruct<Solution>();
        var vfunc = (JToken expect, JToken result) => {
            var arguments = work.Arguments;
            var commands = arguments[0] as IList<string>;
            for (int i = 1; i < commands.Count; ++i) {
                var cmd = commands[i];
                if (cmd == "shuffle") {
                    var evalues = expect[i].ToObject<IList<int>>();
                    var rvalues = result[i].ToObject<IList<int>>();
                    if (!Validators.ForList<int>(false)(evalues, rvalues)) {
                        return false;
                    }
                }
            }
            return true;
        };
        work.SetValidator(vfunc);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
