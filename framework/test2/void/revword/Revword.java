import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    public void reverseWords(char[] s) {
        if (s.length == 0) {
            return;
        }

        reverse(s, 0, s.length - 1);

        int N = s.length;
        int i = 0, j = 0;
        while (j < N) {
            if (s[j] == ' ') {
                reverse(s, i, j-1);
                i = j + 1;
            }
            ++j;
        }
        if (i < j) {
            reverse(s, i, j-1);
        }
    }

    void reverse(char[] s, int i, int j) {
        while (i < j) {
            char temp = s[i];
            s[i] = s[j];
            s[j] = temp;
            ++i;
            --j;
        }
    }
}

public class Revword implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create1u(new Solution()::reverseWords);
        // var work = new TestWork(new Solution(), "reverseWords");
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        // work.setArgumentParser(index, a -> { ... });
        // work.setResultParser(r -> { ... });
        // work.setResultSerializer(r -> {...});
        return work.run(text);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Revword().apply(Utils.fromStdin()));
    }

}
