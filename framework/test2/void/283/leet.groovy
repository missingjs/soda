import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    void moveZeroes(int[] nums) {
        int p = 0
        for (int i = 0; i < nums.length; ++i) {
            if (nums[i] != 0) {
                if (i != p) {
                    int temp = nums[p]
                    nums[p] = nums[i]
                    nums[i] = temp
                }
                ++p
            }
        }
        for (; p < nums.length; ++p) {
            nums[p] = 0
        }
    }
}

def work = TestWork.create(new Solution().&moveZeroes)
// def work = TestWork.forStruct(STRUCT)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
