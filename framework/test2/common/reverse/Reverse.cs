using Soda.Unittest;

public class Solution {
    public string ReverseVowels(string s)
    {
        bool[] isv = new bool[128];
        string vs = "aeiouAEIOU";
        for (int _i = 0; _i < vs.Length; ++_i) {
            isv[vs[_i]] = true;
        }
        char[] buf = s.ToCharArray();
        int i = 0, j = s.Length - 1;
        while (i < j) {
            while (i < j && !isv[buf[i]]) {
                ++i;
            }
            while (i < j && !isv[buf[j]]) {
                --j;
            }
            if (i < j) {
                char temp = buf[i];
                buf[i] = buf[j];
                buf[j] = temp;
                ++i;
                --j;
            }
        }
        return new string(buf);
    }
}

public class Reverse
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().ReverseVowels));
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
