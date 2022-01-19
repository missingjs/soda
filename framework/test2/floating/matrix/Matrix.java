import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class Solution {
    public double[][] matrixMultiply(double[][] a, double[][] b) {
        int rows = a.length, cols = b[0].length;
        var res = new double[rows][cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                double c = 0.0;
                for (int k = 0; k < b.length; ++k) {
                    c += a[i][k] * b[k][j];
                }
                res[i][j] = c;
            }
        }
        return res;
    }
}

public class Matrix implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "matrixMultiply");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        // work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Matrix().get().run();
    }

}
