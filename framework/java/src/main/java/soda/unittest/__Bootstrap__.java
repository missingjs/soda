package soda.unittest;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class Solution {}

public class __Bootstrap__ implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "METHOD");
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        // work.setArgumentParser(index, a -> { ... });
        // work.setResultParser(r -> { ... });
        // work.setResultSerializer(r -> {...});
        return work;
    }

    public static void main(String[] args) throws Exception {
        new __Bootstrap__().get().run();
    }

}
