const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

// step [1]: implement solution function
/**
 * @param {number[]} persons
 * @param {number[]} times
 */
var TopVotedCandidate = function(persons, times) {
    this.N = persons.length;
    this.times = times;
    this.winner = Array(this.N).fill(0);
    let counter = Array(this.N+1).fill(0);
    let win = 0;
    for (let i = 0; i < this.N; ++i) {
        ++counter[persons[i]];
        if (counter[persons[i]] >= counter[win]) {
            win = persons[i];
        }
        this.winner[i] = win;
    }
};

/** 
 * @param {number} t
 * @return {number}
 */
TopVotedCandidate.prototype.q = function(t) {
    if (t >= this.times.at(-1)) {
        return this.winner[this.N-1];
    }
    let [low, high] = [0, this.N-1];
    while (low < high) {
        let mid = Math.floor((low + high) / 2);
        if (t <= this.times[mid]) {
            high = mid;
        } else {
            low = mid + 1;
        }
    }
    return t == this.times[low] ? this.winner[low] : this.winner[low-1];
};

// step [2]: setup function/return/arguments
// const work = TestWork.create(add);
work = TestWork.forStruct(TopVotedCandidate);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

