import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.TestWork;


class Solution {
    public List<Double> multiply(double[] a, double[] b) {
        var res = new double[a.length];
        for (int i = 0; i < res.length; ++i) {
            res[i] = a[i] * b[i];
        }
        // return res;
        return Arrays.stream(res).boxed().collect(Collectors.toList());
    }
}

public class Numeric implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "multiply");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator(Validators.forDoubleArray(true));
        // work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Numeric().get().run();
    }

}
