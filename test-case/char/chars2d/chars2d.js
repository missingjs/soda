const {TestWork, Utils} = require('soda/unittest');

/**
 * @param {character[][]} matrix
 * @return {character[][]}
 */
var toUpper = function(matrix) {
    let diff = 'a'.charCodeAt(0) - 'A'.charCodeAt(0);
    for (let line of matrix) {
        for (let i = 0; i < line.length; ++i) {
            line[i] = String.fromCharCode(line[i].charCodeAt(0) - diff);
        }
    }
    return matrix;
};

const work = TestWork.create(toUpper);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

