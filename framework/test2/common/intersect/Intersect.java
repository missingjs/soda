import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;


class Solution {
    public List<Integer> intersection(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return intersection(nums2, nums1);
        }
        var mset = new HashSet<Integer>();
        var res = new HashSet<Integer>();
        for (int n : nums1) {
            mset.add(n);
        }
        for (int b : nums2) {
            if (mset.contains(b)) {
                res.add(b);
            }
        }
        return new ArrayList<>(res);
    }
}

public class Intersect implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "intersection");
        // var work = TestWork.forStruct(Struct.class);
        work.setValidator(Validators.forList(Integer.class, false));
        // work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Intersect().get().run();
    }

}
