const {TestWork, Utils} = require('soda/unittest');

/**
 * @param {character[]} chars
 * @return {character[]}
 */
var doubleList = function(chars) {
    return chars.concat(chars);
};

const work = TestWork.create(doubleList);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

