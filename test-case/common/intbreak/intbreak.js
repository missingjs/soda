const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {number} n
 * @return {number}
 */
var integerBreak = function(n) {
    memo = new Array(59).fill(0);
    return solve(n);
};

var solve = function(n) {
    if (n == 1) {
        return 1;
    }
    if (memo[n] > 0) {
        return memo[n];
    }
    let res = 0;
    for (let i = 1; i < n; ++i) {
        res = Math.max(i * (n-i), i * solve(n-i), res);
    }
    memo[n] = res;
    return res;
};

let memo = [];

const work = TestWork.create(integerBreak);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

