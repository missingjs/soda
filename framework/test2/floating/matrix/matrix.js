const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {number[][]} a
 * @param {number[][]} b
 * @return {number[][]}
 */
var matrixMultiply = function(a, b) {
    let rows = a.length;
    let cols = b[0].length;
    let res = Array(rows).fill(0).map(() => Array(cols).fill(0));
    for (let i = 0; i < rows; ++i) {
        for (let j = 0; j < cols; ++j) {
            let c = 0;
            for (let k = 0; k < b.length; ++k) {
                c += a[i][k] * b[k][j];
            }
            res[i][j] = c;
        }
    }
    return res;
};

const work = TestWork.create(matrixMultiply);
// work.validator = (x, y) => { ... };
// work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

