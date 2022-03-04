const {
    AvlTree,
    BinarySearchTree
} = require('@datastructures-js/binary-search-tree');
const {
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');
const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

// step [1]: implement solution function
class VirIndex {
    constructor(nums) {
        this.nums = nums;
    }

    get(index) {
        return this.nums[this.#mapIndex(index)];
    }

    set(index, value) {
        this.nums[this.#mapIndex(index)] = value;
    }

    #mapIndex(i) {
        let N = this.nums.length;
        if ((N&1) == 1 || i > ((N-1)>>1)) {
            return (((N-i) << 1) - 1) % N;
        } else {
            return N - 2 - (i << 1);
        }
    }
}
/**
 * @param {number[]} nums
 * @return {void} Do not return anything, modify nums in-place instead.
 */
var wiggleSort = function(nums) {
    let vi = new VirIndex(nums);
    let N = nums.length;
    quickSelect(vi, 0, N-1, Math.floor((N-1)/2));
};

function quickSelect(vi, start, end, k) {
    while (start < end) {
        let [p0, p1] = partition(vi, start, end);
        if (k >= p0 && k <= p1) {
            return;
        }
        if (k > p1) {
            start = p1 + 1;
        } else {
            end = p0 - 1;
        }
    }
}

function partition(vi, start, end) {
    let mid = Math.floor((start + end) / 2);
    let pivot = getMedian(vi.get(start), vi.get(mid), vi.get(end));
    let p = start;
    let z = end + 1;
    for (let q = start; q < z; ) {
        if (vi.get(q) < pivot) {
            let temp = vi.get(p);
            vi.set(p, vi.get(q));
            vi.set(q, temp);
            ++p;
            ++q;
        } else if (vi.get(q) == pivot) {
            ++q;
        } else {
            --z;
            let temp = vi.get(z);
            vi.set(z, vi.get(q));
            vi.set(q, temp);
        }
    }
    return [p, z-1];
}

function getMedian(a, b, c) {
    if (a >= b) {
        return b >= c ? b : Math.min(a, c);
    } else {
        return a >= c ? a : Math.min(b, c);
    }
}

// step [2]: setup function/return/arguments
const work = TestWork.create(wiggleSort);
// work = TestWork.forStruct(STRUCT);
work.validator = (_, nums) => {
    for (let i = 1; i < nums.length; ++i) {
        if (i % 2 != 0 && nums[i] <= nums[i-1]
                || i % 2 == 0 && nums[i] >= nums[i-1]) {
            return false;
        }
    }
    return true;
};
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

