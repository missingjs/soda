import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

class Solution {
    public List<Character> doubleList(char[] chars) {
        var res = new ArrayList<Character>();
        for (char ch : chars) {
            res.add(ch);
        }
        for (char ch : chars) {
            res.add(ch);
        }
        return res;
    }
}

public class Chars1d implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "doubleList");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Chars1d().get().run();
    }

}
