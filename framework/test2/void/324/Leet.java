import soda.unittest.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.*;

class VirIndex {
    private int N;
    private int[] nums;

    public VirIndex(int[] nums) {
        this.nums = nums;
        N = nums.length;
    }

    public int get(int index) {
        return nums[mapIndex(index)];
    }

    public void set(int index, int value) {
        nums[mapIndex(index)] = value;
    }

    private int mapIndex(int i) {
        if ((N&1) == 1 || i > ((N-1)>>1)) {
            return (((N-i) << 1) - 1) % N;
        } else {
            return (N - 2 - (i << 1));
        }
    }
}

class Solution {
    public void wiggleSort(int[] nums) {
        method2(nums);
    }

    public void method2(int[] nums) {
        var vi = new VirIndex(nums);
        int N = nums.length;
        quickSelect(vi, 0, N-1, (N-1)/2);
    }

    private void quickSelect(VirIndex vi, int start, int end, int k) {
        while (start < end) {
            int[] p = partition(vi, start, end);
            if (k >= p[0] && k <= p[1]) {
                return;
            }
            if (k > p[1]) {
                start = p[1] + 1;
            } else {
                end = p[0] - 1;
            }
        }
    }

    private int[] partition(VirIndex vi, int start, int end) {
        int mid = (start + end) / 2;
        int pivot = getMedian(vi.get(start), vi.get(mid), vi.get(end));
        int p = start, z = end+1;
        for (int q = start; q < z; ) {
            if (vi.get(q) < pivot) {
                int temp = vi.get(p);
                vi.set(p, vi.get(q));
                vi.set(q, temp);
                ++p;
                ++q;
            } else if (vi.get(q) == pivot) {
                ++q;
            } else {
                --z;
                int temp = vi.get(z);
                vi.set(z, vi.get(q));
                vi.set(q, temp);
            }
        }
        return new int[] {p, z-1};
    }

    private int getMedian(int a, int b, int c) {
        if (a >= b) {
            return b >= c ? b : Math.min(a, c);
        } else {
            return a >= c ? a : Math.min(b, c);
        }
    }

    public void method1(int[] nums) {
        int N = nums.length;
        Arrays.sort(nums);
        int mid = (nums.length - 1) / 2;
        int[] res = new int[nums.length];

        for (int k = 0, i = mid; k < N; --i, k += 2) {
            res[k] = nums[i];
        }
        for (int k = 1, i = N-1; k < N; --i, k += 2) {
            res[k] = nums[i];
        }

        System.arraycopy(res, 0, nums, 0, N);
    }
}

public class Leet implements Function<String, String> {

    @Override
    public String apply(String text) {
        var work = GenericTestWork.create1u(new Solution()::wiggleSort);
        // var work = GenericTestWork.forStruct(Solution.class);
        // var work = TestWork.forObject(new Solution(), "METHOD");
        // var work = TestWork.forStruct(Struct.class);
        work.setValidator((e, r) -> Leet.validate(r));
        work.setCompareSerial(true);
        return work.run(text);
    }

    private static boolean validate(int[] nums) {
        for (int i = 1; i < nums.length; ++i) {
            if (i % 2 != 0 && nums[i] <= nums[i-1] || i % 2 == 0 && nums[i] >= nums[i-1]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new Leet().apply(Utils.fromStdin()));
    }

}
