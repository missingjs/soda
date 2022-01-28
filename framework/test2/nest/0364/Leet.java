import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.TestWork;


class Solution {
    public int depthSumInverse(List<NestedInteger> nestedList) {
        return method2(nestedList);
    }

    public static class Info {
        public int sum, product, maxDepth;
        public Info(int s, int p, int m) {
            sum = s;
            product = p;
            maxDepth = m;
        }
    }

    public int method2(List<NestedInteger> nestedList) {
        var info = getInfo(nestedList, 1);
        return (info.maxDepth + 1) * info.sum - info.product;
    }

    private Info getInfo(List<NestedInteger> nestedList, int depth) {
        int sum = 0, product = 0, maxDepth = depth;
        for (var ni : nestedList) {
            if (ni.isInteger()) {
                int val = ni.getInteger();
                sum += val;
                product += val * depth;
                maxDepth = Math.max(maxDepth, depth);
            } else {
                var res = getInfo(ni.getList(), depth+1);
                sum += res.sum;
                product += res.product;
                maxDepth = Math.max(maxDepth, res.maxDepth);
            }
        }
        return new Info(sum, product, maxDepth);
    }

    public int method1(List<NestedInteger> nestedList) {
        int maxDepth = 0;
        for (var ni : nestedList) {
            maxDepth = Math.max(maxDepth, getDepth(ni, 1));
        }
        int res = 0;
        for (var ni : nestedList) {
            res += calc(ni, 1, maxDepth);
        }
        return res;
    }

    private int calc(NestedInteger ni, int depth, int maxDepth) {
        if (ni.isInteger()) {
            return ni.getInteger() * (maxDepth - depth + 1);
        }
        int res = 0;
        for (var j : ni.getList()) {
            res += calc(j, depth+1, maxDepth);
        }
        return res;
    }

    private int getDepth(NestedInteger ni, int depth) {
        if (ni.isInteger()) {
            return depth;
        }
        int d = depth;
        for (var j : ni.getList()) {
            d = Math.max(d, getDepth(j, depth+1));
        }
        return d;
    }
}

public class Leet implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "depthSumInverse");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Leet().get().run();
    }

}
