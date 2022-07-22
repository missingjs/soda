import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.TestWork;

class Solution {
    public char nextChar(char ch) {
        return (char) (ch + 1);
    }
}

public class Single implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "nextChar");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Single().get().run();
    }

}
