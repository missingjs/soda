import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class Solution {
    public void moveZeroes(int[] nums) {
        int p = 0;
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] != 0) {
                if (i != p) {
                    int temp = nums[p];
                    nums[p] = nums[i];
                    nums[i] = temp;
                }
                ++p;
            }
        }
        for (; p < nums.length; ++p) {
            nums[p] = 0;
        }
    }
}

public class Leet implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create(new Solution()::moveZeroes);
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
