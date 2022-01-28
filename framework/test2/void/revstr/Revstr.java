import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;
import soda.unittest.work.Utils;


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

public class Revstr implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = TestWork.forObject(new Solution(), "reverseString");
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work.run(text);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Revstr().apply(Utils.fromStdin()));
    }

}
