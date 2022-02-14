const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

// step [1]: implement solution function
/**
 * @param {string[]} s
 * @return {void}
 */
var reverseString = function(s) {
    let [i, j] = [0, s.length-1];
    while (i < j) {
        [s[i], s[j]] = [s[j], s[i]];
        ++i;
        --j;
    }
};

// step [2]: setup function/return/arguments
const work = TestWork.create(reverseString);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

