import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} from '@datastructures-js/priority-queue';
import { ListNode, NestedInteger, TreeNode } from 'soda/leetcode';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
class VirIndex {
    constructor(private nums: number[]) {
    }

    get(index: number): number {
        return this.nums[this.mapIndex(index)];
    }

    set(index: number, value: number) {
        this.nums[this.mapIndex(index)] = value;
    }

    private mapIndex(i: number): number {
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
function wiggleSort(nums: number[]): void {
    let vi = new VirIndex(nums);
    let N = nums.length;
    quickSelect(vi, 0, N-1, Math.floor((N-1)/2));
}

function quickSelect(vi: VirIndex, start: number, end: number, k: number) {
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

function partition(vi: VirIndex, start: number, end: number): number[] {
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

function getMedian(a: number, b: number, c: number): number {
    if (a >= b) {
        return b >= c ? b : Math.min(a, c);
    } else {
        return a >= c ? a : Math.min(b, c);
    }
}

// step [2]: setup function/return/arguments
let taskFunc = wiggleSort;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
work.validator = (_: number[], nums: number[]) => {
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
