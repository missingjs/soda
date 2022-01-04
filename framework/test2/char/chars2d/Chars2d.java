import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class Solution {
    public char[][] toUpper(List<List<Character>> matrix) {
        int diff = 'a' - 'A';
        char[][] mx = new char[matrix.size()][matrix.get(0).size()];
        for (int i = 0; i < matrix.size(); ++i) {
            for (int j = 0; j < matrix.get(0).size(); ++j) {
                mx[i][j] = (char) (matrix.get(i).get(j) - diff);
            }
        }
        return mx;
    }
}

public class Chars2d implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "toUpper");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Chars2d().get().run();
    }

}
