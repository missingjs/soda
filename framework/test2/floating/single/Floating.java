import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class Solution {
    public double divide(double a, double b) {
        return a / b;
    }
}

public class Floating implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "divide");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        // work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Floating().get().run();
    }

}
