import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.TestWork;


class Solution {
    public String reverseVowels(String s) {
        boolean[] isv = new boolean[128];
        String vs = "aeiouAEIOU";
        for (int i = 0; i < vs.length(); ++i) {
            isv[vs.charAt(i)] = true;
        }
        char[] buf = s.toCharArray();
        int i = 0, j = s.length()-1;
        while (i < j) {
            while (i < j && !isv[buf[i]]) {
                ++i;
            }
            while (i < j && !isv[buf[j]]) {
                --j;
            }
            if (i < j) {
                char temp = buf[i];
                buf[i] = buf[j];
                buf[j] = temp;
                ++i;
                --j;
            }
        }
        return new String(buf);
    }
}

public class Reverse implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "reverseVowels");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Reverse().get().run();
    }

}
