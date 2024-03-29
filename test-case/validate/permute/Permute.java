import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    public String[] permutation(char[] chars, int n) {
        var res = new ArrayList<String>();
        char[] buf = new char[n];
        solve(chars, 0, buf, 0, res);
        return res.toArray(new String[0]);
    }

    private void solve(char[] chars, int i, char[] buf, int j, List<String> res) {
        if (j == buf.length) {
            res.add(new String(buf));
            return;
        }
        for (int k = i; k < chars.length; ++k) {
            char temp = chars[i];
            chars[i] = chars[k];
            chars[k] = temp;
            buf[j] = chars[i];
            solve(chars, i+1, buf, j+1, res);
            temp = chars[i];
            chars[i] = chars[k];
            chars[k] = temp;
        }
    }
}

public class Permute implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create(new Solution()::permutation);
        // var work = GenericTestWork.create2(Solution.class, "permutation", new Solution()::permutation);
        // var work = new TestWork(new Solution(), "permutation");
        // var work = TestWork.forStruct(Struct.class);
        work.setValidator(Validators.forArray(String.class, false));
        work.setCompareSerial(true);
        return work.run(text);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Permute().apply(Utils.fromStdin()));
    }

}
