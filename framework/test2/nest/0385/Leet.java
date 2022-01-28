import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;


class Solution {
    private int p;
    public NestedInteger deserialize(String s) {
        p = 0;
        return parse(s);
    }

    private NestedInteger parse(String s) {
        if (s.charAt(p) == '[') {
            ++p;
            var root = new DefaultNestedInteger();
            while (s.charAt(p) != ']') {
                root.add(parse(s));
                if (s.charAt(p) == ',') {
                    ++p;
                }
            }
            ++p;
            return root;
        }

        boolean negative = false;
        if (s.charAt(p) == '-') {
            ++p;
            negative = true;
        }

        int value = 0;
        while (p < s.length() && s.charAt(p) >= '0' && s.charAt(p) <= '9') {
            value = value * 10 + s.charAt(p) - '0';
            ++p;
        }

        if (negative) {
            value = 0 - value;
        }
        return new DefaultNestedInteger(value);
    }
}

public class Leet implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "deserialize");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Leet().get().run();
    }

}
