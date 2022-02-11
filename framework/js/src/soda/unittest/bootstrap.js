const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {number} a
 * @param {number} b
 * @return {number}
 */
var add = function(a, b) {
    return a + b;
};

const work = TestWork.create(add);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

