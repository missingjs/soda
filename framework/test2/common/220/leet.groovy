import soda.groovy.unittest.*

class Solution {
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        def map = new TreeMap<Long, Integer>()
        def (i, j) = [0, 0]
        while (j < nums.length) {
            if (j - i <= k) {
                long val = nums[j++]
                long lower = val - t
                long upper = val + t
                def ceil = map.ceilingKey(lower)
                if (ceil != null && ceil <= upper) {
                    return true
                }
                map.put(val, map.getOrDefault(val, 0) + 1)
            } else {
                long val = nums[i++]
                def c = map[val] - 1
                if (c == 0) {
                    map.remove(val)
                } else {
                    map[val] = c
                }
            }
        }
        false
    }
}

class LeetWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&containsNearbyAlmostDuplicate)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        work.compareSerial = true
        work.run(input)
    }
}

println new LeetWork()(System.in.getText('UTF-8'))
