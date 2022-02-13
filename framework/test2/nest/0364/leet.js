const {ListNode, NestedInteger} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {NestedInteger[]} nestedList
 * @return {number}
 */
var depthSumInverse = function(nestedList) {
    let info = getInfo(nestedList, 1);
    return (info.maxDepth + 1) * info.sum - info.product;
};

function Info(s, p, m) {
    this.sum = s;
    this.product = p;
    this.maxDepth = m;
}

function getInfo(nestedList, depth) {
    let sum = 0;
    let product = 0;
    let maxDepth = depth;
    for (let ni of nestedList) {
        if (ni.isInteger()) {
            let val = ni.getInteger();
            sum += val;
            product += val * depth;
            maxDepth = Math.max(maxDepth, depth);
        } else {
            let res = getInfo(ni.getList(), depth+1);
            sum += res.sum;
            product += res.product;
            maxDepth = Math.max(maxDepth, res.maxDepth);
        }
    }
    return new Info(sum, product, maxDepth);
}

const work = TestWork.create(depthSumInverse);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

