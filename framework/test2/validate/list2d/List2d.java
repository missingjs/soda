import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class Solution {
    public String[][] groupByLength(String[] strs) {
        var strList = Arrays.asList(strs);
        Collections.shuffle(strList);
        var group = new HashMap<Integer, List<String>>();
        for (var s : strList) {
            group.computeIfAbsent(s.length(), k -> new ArrayList<>()).add(s);
        }
        var keys = new ArrayList<>(group.keySet());
        Collections.shuffle(keys);
        var res = new String[group.size()][];
        for (int i = 0; i < keys.size(); ++i) {
            res[i] = group.get(keys.get(i)).toArray(new String[0]);
        }
        return res;
    }
}

public class List2d implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "groupByLength");
        // var work = TestWork.forStruct(Struct.class);
        work.setValidator(Validators.forArray2d(String.class, false, false));
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new List2d().get().run();
    }

}
