import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;


class SummaryRanges {

    private int[] parent;

    private Set<Integer> ancestorSet = new HashSet<>();

    public SummaryRanges() {
        parent = new int[10003];
    }
    
    public void addNum(int val) {
        ++val;

        if (parent[val] != 0) {
            return;
        }

        parent[val] = -1;
        ancestorSet.add(val);

        int left = val - 1, right = val + 1;
        if (left > 0 && parent[left] != 0) {
            merge(left, val);
        }
        if (parent[right] != 0) {
            merge(val, right);
        }
    }

    public int[][] getIntervals() {
        var ans = new ArrayList<>(ancestorSet);
        Collections.sort(ans);
        int[][] res = new int[ans.size()][];
        for (int i = 0; i < res.length; ++i) {
            int start = ans.get(i);
            int end = start - parent[start] - 1;
            res[i] = new int[] { start-1, end-1 };
        }
        return res;
    }

    private void merge(int x, int y) {
        int ax = getAncestor(x), ay = getAncestor(y);
        if (ax < ay) {
            mergeAncestor(ax, ay);
        } else {
            mergeAncestor(ay, ax);
        }
    }

    private void mergeAncestor(int ax, int ay) {
        parent[ax] += parent[ay];
        parent[ay] = ax;
        ancestorSet.remove(ay);
    }

    private int getAncestor(int x) {
        return parent[x] < 0 ? x : (parent[x] = getAncestor(parent[x]));
    }

}

public class Leet implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        // var work = new TestWork(new Solution(), "METHOD");
        var work = TestWork.forStruct(SummaryRanges.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Leet().get().run();
    }

}
