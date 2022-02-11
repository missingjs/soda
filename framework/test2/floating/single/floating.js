const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {number} a
 * @param {number} b
 * @return {number}
 */
var divide = function(a, b) {
    return a / b;
};

const work = TestWork.create(divide);
// work.validator = (x, y) => { ... };
// work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

