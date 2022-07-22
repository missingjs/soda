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
/**
 * @param {number[]} nums
 * @return {void} Do not return anything, modify nums in-place instead.
 */
function moveZeroes(nums: number[]): void {
    let p = 0;
    for (let i = 0; i < nums.length; ++i) {
        if (nums[i] != 0) {
            if (i != p) {
                [nums[i], nums[p]] = [nums[p], nums[i]];
            }
            ++p;
        }
    }
    while (p < nums.length) {
        nums[p] = 0;
        ++p;
    }
}

// step [2]: setup function/return/arguments
let taskFunc = moveZeroes;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
