import soda.groovy.unittest.*

class Solution {
    public List<Integer> intersection(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return intersection(nums2, nums1)
        }
        def mset = new HashSet<Integer>()
        def res = new HashSet<Integer>()
        for (n in nums1) {
            mset << n
        }
        for (b in nums2) {
            if (b in mset) {
                res << b
            }
        }
        res.toList()
    }
}

def work = TestWork.create(new Solution().&intersection)
work.validator = Validators.forList(Integer, false)
println work.run(System.in.getText('UTF-8'))
