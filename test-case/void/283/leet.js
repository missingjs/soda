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
/**
 * @param {number[]} nums
 * @return {void} Do not return anything, modify nums in-place instead.
 */
var moveZeroes = function(nums) {
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
};

// step [2]: setup function/return/arguments
const work = TestWork.create(moveZeroes);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

