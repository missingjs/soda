using Soda.Unittest;
using Soda.Leetcode;


public class Solution {

    private int p;

    public NestedInteger Deserialize(string s) {
        p = 0;
        return parse(s);
    }

    private NestedInteger parse(string s) {
        if (s[p] == '[') {
            ++p;
            var root = new NestedInteger();
            while (s[p] != ']') {
                root.Add(parse(s));
                if (s[p] == ',') {
                    ++p;
                }
            }
            ++p;
            return root;
        }

        bool negative = false;
        if (s[p] == '-') {
            ++p;
            negative = true;
        }

        int value = 0;
        while (p < s.Length && s[p] >= '0' && s[p] <= '9') {
            value = value * 10 + s[p] - '0';
            ++p;
        }

        if (negative) {
            value = -value;
        }
        return new NestedInteger(value);
    }
}

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().Deserialize));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
