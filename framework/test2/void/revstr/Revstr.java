import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class Solution {
    public void reverseString(char[] s) {
        int i = 0, j = s.length-1;
        while (i < j) {
            char temp = s[i];
            s[i] = s[j];
            s[j] = temp;
            ++i;
            --j;
        }
    }
}

public class Revstr implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "reverseString");
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        // work.setArgumentParser(index, a -> { ... });
        // work.setResultParser(r -> { ... });
        // work.setResultSerializer(r -> {...});
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Revstr().get().run();
    }

}
