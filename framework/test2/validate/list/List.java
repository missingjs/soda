import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;


class Solution {
    public String[] permutation(char[] chars, int n) {
        var res = new ArrayList<String>();
        char[] buf = new char[n];
        solve(chars, 0, buf, 0, res);
        return res.toArray(new String[0]);
    }

    private void solve(char[] chars, int i, char[] buf, int j, java.util.List<String> res) {
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

public class List implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "permutation");
        // var work = TestWork.forStruct(Struct.class);
        work.setValidator(Validators.forArray(String.class, false));
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new List().get().run();
    }

}
