const {TestWork, Utils} = require('soda/unittest');

/**
 * @param {character} ch
 * @return {character}
 */
var nextChar = function(ch) {
    return String.fromCharCode(ch.charCodeAt(0) + 1);
};

const work = TestWork.create(nextChar);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

