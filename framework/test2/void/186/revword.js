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
var reverseWords = function(s) {
    if (s.length == 0) {
        return;
    }
    reverse(s, 0, s.length-1);
    let N = s.length;
    [i, j] = [0, 0];
    while (j < N) {
        if (s[j] == ' ') {
            reverse(s, i, j-1);
            i = j + 1;
        }
        ++j;
    }
    if (i < j) {
        reverse(s, i, j-1);
    }
};

function reverse(s, i, j) {
    while (i < j) {
        [s[i], s[j]] = [s[j], s[i]];
        ++i;
        --j;
    }
}

// step [2]: setup function/return/arguments
const work = TestWork.create(reverseWords);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

