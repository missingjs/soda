const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

// step [1]: implement solution function
/**
 * @param {string[]} strs
 * @return {string[][]}
 */
var groupByLength = function(strs) {
    shuffle(strs);
    let group = new Map();
    for (let s of strs) {
        if (!group.has(s.length)) {
            group.set(s.length, []);
        }
        group.get(s.length).push(s);
    }
    let keys = [...group.keys()];
    shuffle(keys);
    return keys.map(x => group.get(x));
};

function shuffle(arr) {
    for (let i = arr.length; i > 0; --i) {
        let index = Math.floor(Math.random() * i);
        if (index != i-1) {
            [arr[index], arr[i-1]] = [arr[i-1], arr[index]];
        }
    }
}

// step [2]: setup function/return/arguments
const work = TestWork.create(groupByLength);
// work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray2d('string', false, false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

