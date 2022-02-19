import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        var map = new TreeMap<Long, Integer>();
        int i = 0, j = 0;
        while (j < nums.length) {
            if (j - i <= k) {
                long val = nums[j];
                ++j;
                long lower = val - t, upper = val + t;
                Long ceil = map.ceilingKey(lower);
                if (ceil != null && ceil <= upper) {
                    return true;
                }
                map.put(val, map.getOrDefault(val, 0) + 1);
            } else {
                long val = nums[i];
                ++i;
                int count = map.get(val);
                if (count == 1) {
                    map.remove(val);
                } else {
                    map.put(val, count-1);
                }
            }
        }
        return false;
    }
}

public class Leet implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create3(new Solution()::containsNearbyAlmostDuplicate);
        // var work = GenericTestWork.forStruct(Solution.class);
        // var work = TestWork.forObject(new Solution(), "METHOD");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        return work.run(text);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Leet().apply(Utils.fromStdin()));
    }

}
