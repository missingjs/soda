const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {number[]} a
 * @param {number[]} b
 * @return {number[]}
 */
var multiply = function(a, b) {
    return a.map((elem, idx) => elem * b[idx]);
};

const work = TestWork.create(multiply);
// work.validator = (x, y) => { ... };
// work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

