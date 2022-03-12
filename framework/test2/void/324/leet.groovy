import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class VirIndex {
    private int N
    private int[] nums

    VirIndex(int[] nums) {
        this.nums = nums
        N = nums.length
    }

    int getAt(int index) {
        return nums[mapIndex(index)]
    }

    void putAt(int index, int value) {
        nums[mapIndex(index)] = value
    }

    private int mapIndex(int i) {
        if ((N&1) == 1 || i > ((N-1)>>1)) {
            (((N-i) << 1) - 1) % N
        } else {
            (N - 2 - (i << 1))
        }
    }
}

class Solution {
    void wiggleSort(int[] nums) {
        def vi = new VirIndex(nums)
        int N = nums.length
        quickSelect(vi, 0, N-1, (N-1)/2 as int)
    }

    private void quickSelect(VirIndex vi, int start, int end, int k) {
        while (start < end) {
            int[] p = partition(vi, start, end)
            if (k >= p[0] && k <= p[1]) {
                return
            }
            if (k > p[1]) {
                start = p[1] + 1
            } else {
                end = p[0] - 1
            }
        }
    }

    private int[] partition(VirIndex vi, int start, int end) {
        int mid = (start + end) / 2
        int pivot = getMedian(vi[start], vi[mid], vi[end])
        int p = start, z = end+1
        for (int q = start; q < z; ) {
            if (vi[q] < pivot) {
                int temp = vi[p]
                vi[p] = vi[q]
                vi[q] = temp
                ++p
                ++q
            } else if (vi[q] == pivot) {
                ++q
            } else {
                --z
                int temp = vi[z]
                vi[z] = vi[q]
                vi[q] = temp
            }
        }
        new int[] {p, z-1}
    }

    private int getMedian(int a, int b, int c) {
        if (a >= b) {
            b >= c ? b : Math.min(a, c)
        } else {
            a >= c ? a : Math.min(b, c)
        }
    }
}

class LeetWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&wiggleSort)
        // def work = TestWork.forStruct(STRUCT)
        work.validator = { e, nums ->
            def res = true
            for (int i = 1; i < nums.length; ++i) {
                if (i % 2 != 0 && nums[i] <= nums[i-1] || i % 2 == 0 && nums[i] >= nums[i-1]) {
                    res = false
                    break
                }
            }
            res
        }
        work.compareSerial = true
        work.run(input)
    }
}

println new LeetWork()(System.in.getText('UTF-8'))
