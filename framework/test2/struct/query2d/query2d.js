const {ListNode, NestedInteger, TreeNode} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');
const {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} = require('@datastructures-js/priority-queue');

// step [1]: implement solution function
/**
 * @param {number[][]} matrix
 */
var NumMatrix = function(matrix) {
    this.rows = matrix.length + 1;
    this.cols = matrix[0].length + 1;
    this.mx = Array(matrix.length).fill(0).map(line => Array(matrix[0].length).fill(0));
    this.bit = Array(this.rows).fill(0).map(line => Array(this.cols).fill(0));
    for (let i = 0; i < this.rows-1; ++i) {
        for (let j = 0; j < this.cols-1; ++j) {
            this.update(i, j, matrix[i][j]);
        }
    }
};

/**
 * @param {number} row
 * @param {number} col
 * @param {number} val
 * @return {void}
 */
NumMatrix.prototype.update = function(row, col, val) {
    let diff = val - this.mx[row][col];
    this.mx[row][col] = val;
    let i = row + 1;
    while (i < this.rows) {
        let j = col + 1;
        while (j < this.cols) {
            this.bit[i][j] += diff;
            j += (j & -j);
        }
        i += (i & -i);
    }
};

/**
 * @param {number} row1
 * @param {number} col1
 * @param {number} row2
 * @param {number} col2
 * @return {number}
 */
NumMatrix.prototype.sumRegion = function(row1, col1, row2, col2) {
    return this.query(row1, col1) - this.query(row1, col2+1) - this.query(row2+1, col1) + this.query(row2+1, col2+1);
};

NumMatrix.prototype.query = function(r, c) {
    let res = 0;
    let i = r;
    while (i > 0) {
        let j = c;
        while (j > 0) {
            res += this.bit[i][j];
            j -= (j & -j);
        }
        i -= (i & -i);
    }
    return res;
};

// step [2]: setup function/return/arguments
// const work = TestWork.create(add);
work = TestWork.forStruct(NumMatrix);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

