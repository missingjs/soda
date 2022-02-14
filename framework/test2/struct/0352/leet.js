const {ListNode, NestedInteger} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

var SummaryRanges = function() {
    this.parent = Array(10003).fill(0);
    this.ancestorSet = new Set();
};

/** 
 * @param {number} val
 * @return {void}
 */
SummaryRanges.prototype.addNum = function(val) {
    ++val;
    if (this.parent[val] != 0) {
        return;
    }
    this.parent[val] = -1;
    this.ancestorSet.add(val);
    let left = val - 1;
    let right = val + 1;
    if (left > 0 && this.parent[left] != 0) {
        this.merge(left, val);
    }
    if (this.parent[right] != 0) {
        this.merge(val, right);
    }
};

/**
 * @return {number[][]}
 */
SummaryRanges.prototype.getIntervals = function() {
    let ans = [...this.ancestorSet];
    ans.sort((x, y) => x - y);
    let res = Array(ans.length).fill(null);
    for (let i = 0; i < res.length; ++i) {
        let start = ans[i];
        let end = start - this.parent[start] - 1;
        res[i] = [start-1, end-1];
    }
    return res;
};

SummaryRanges.prototype.merge = function(x, y) {
    let ax = this.getAncestor(x);
    let ay = this.getAncestor(y);
    if (ax < ay) {
        this.mergeAncestor(ax, ay);
    } else {
        this.mergeAncestor(ay, ax);
    }
};

SummaryRanges.prototype.mergeAncestor = function(ax, ay) {
    this.parent[ax] += this.parent[ay];
    this.parent[ay] = ax;
    this.ancestorSet.delete(ay);
};

SummaryRanges.prototype.getAncestor = function(x) {
    return this.parent[x] < 0 ? x : (this.parent[x] = this.getAncestor(this.parent[x]));
};

// const work = TestWork.create(add);
const work = TestWork.forStruct(SummaryRanges);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

