import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.TestWork;


class Solution {

    private final int[] original;

    public Solution(int[] nums) {
        original = nums;
    }
    
    public int[] reset() {
        var res = new int[original.length];
        System.arraycopy(original, 0, res, 0, original.length);
        return res;
    }
    
    public int[] shuffle() {
        var res = reset();
        for (int s = res.length; s > 0; --s) {
            int i = (int) (Math.random() * s);
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

public class Leet implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        // var work = new TestWork(new Solution(), "METHOD");
        var work = TestWork.forStruct(Solution.class);
        work.setValidator((List<Object> a, List<Object> b) -> validate(work, a, b));
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Leet().get().run();
    }

    private static boolean validate(TestWork work, List<Object> expect, List<Object> result) {
        Object[] arguments = work.getArguments();
        var commands = (List<String>) arguments[0];
        var listCmp = Validators.forList(Integer.class, false);
        for (int i = 1; i < commands.size(); ++i) {
            String cmd = commands.get(i);
            if (cmd.equals("shuffle")) {
                var evalues = (List<Integer>) expect.get(i);
                var rvalues = (List<Integer>) result.get(i);
                if (!listCmp.test(evalues, rvalues)) {
                    return false;
                }
            }
        }
        return true;
    }

}
