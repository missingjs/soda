import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.TestWork;

class Solution {
    private int[] memo;
    public int integerBreak(int n) {
        memo = new int[59];
        return solve(n);
    }

    private int solve(int n) {
        if (n == 1) {
            return 1;
        }
        if (memo[n] > 0) {
            return memo[n];
        }
        int res = 0;
        for (int i = 1; i < n; ++i) {
            res = Math.max(i * (n-i), res);
            res = Math.max(i * solve(n-i), res);
        }
        memo[n] = res;
        return res;
    }
}

public class Intbreak implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "integerBreak");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Intbreak().get().run();
    }

}
