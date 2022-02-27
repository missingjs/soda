const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

// step [1]: implement solution function
/**
 * @param {number[]} nums
 */
var Solution = function(nums) {
    this.original = nums;
};

/**
 * @return {number[]}
 */
Solution.prototype.reset = function() {
    return [...this.original];
};

/**
 * @return {number[]}
 */
Solution.prototype.shuffle = function() {
    let res = this.reset();
    for (let s = res.length; s > 0; --s) {
        let i = Math.floor(Math.random() * s);
        let j = s - 1;
        if (i != j) {
            [res[i], res[j]] = [res[j], res[i]];
        }
    }
    return res;
};

// step [2]: setup function/return/arguments
// const work = TestWork.create(add);
work = TestWork.forStruct(Solution);
work.validator = (expect, result) => {
    let args = work.arguments;
    let commands = args[0];
    let arrCmp = Validators.forArray('number', false);
    for (let i = 1; i < commands.length; ++i) {
        let cmd = commands[i];
        if (cmd === 'shuffle') {
            let evalues = expect[i];
            let rvalues = result[i];
            if (!arrCmp(evalues, rvalues)) {
                return false;
            }
        }
    }
    return true;
};
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

