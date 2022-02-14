const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

// step [1]: implement solution function
/**
 * @param {character[]} chars
 * @param {number} n
 * @return {character[]}
 */
var permutation = function(chars, n) {
    let res = [];
    let buf = Array(n).fill(null);
    solve(chars, 0, buf, 0, res);
    return res;
};

function solve(chars, i, buf, j, res) {
    if (j == buf.length) {
        res.push(buf.join(''));
        return;
    }
    for (let k = i; k < chars.length; ++k) {
        [chars[i], chars[k]] = [chars[k], chars[i]];
        buf[j] = chars[i];
        solve(chars, i+1, buf, j+1, res);
        [chars[i], chars[k]] = [chars[k], chars[i]];
    }
}

// step [2]: setup function/return/arguments
const work = TestWork.create(permutation);
// work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray('string', false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

