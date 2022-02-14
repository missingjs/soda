const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

// step [1]: implement solution function
/**
 * @param {TreeNode} root
 * @return {number[][]}
 */
var findLeaves = function(root) {
    let res = Array(100).fill(0).map(() => []);
    let r = solve(root, res);
    return res.slice(0, r);
};

function solve(root, res) {
    if (!root) {
        return 0;
    }
    let R = solve(root.right, res);
    let L = solve(root.left, res);
    let index = Math.max(L, R);
    res[index].push(root.val);
    return index + 1;
}

// step [2]: setup function/return/arguments
const work = TestWork.create(findLeaves);
// work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray2d('number', true, false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

