import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} from '@datastructures-js/priority-queue';
import { ListNode, NestedInteger, TreeNode } from 'soda/leetcode';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
class NumMatrix {
    private rows: number;
    private cols: number;
    private mx: number[][];
    private bit: number[][];

    /**
     * @param {number[][]} matrix
     */
    constructor(matrix: number[][]) {
        this.rows = matrix.length + 1;
        this.cols = matrix[0].length + 1;
        this.mx = Array(matrix.length).fill(0).map(line => Array(matrix[0].length).fill(0));
        this.bit = Array(this.rows).fill(0).map(line => Array(this.cols).fill(0));
        for (let i = 0; i < this.rows-1; ++i) {
            for (let j = 0; j < this.cols-1; ++j) {
                this.update(i, j, matrix[i][j]);
            }
        }
    }

    /**
     * @param {number} row
     * @param {number} col
     * @param {number} val
     * @return {void}
     */
    update(row: number, col: number, val: number) {
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
    }

    /**
     * @param {number} row1
     * @param {number} col1
     * @param {number} row2
     * @param {number} col2
     * @return {number}
     */
    sumRegion(row1: number, col1: number, row2: number, col2: number): number {
        return this.query(row1, col1)
            - this.query(row1, col2+1)
            - this.query(row2+1, col1)
            + this.query(row2+1, col2+1);
    }

    query(r: number, c: number): number {
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
    }
}

// step [2]: setup function/return/arguments
// let taskFunc = add;
// const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
let work = TestWork.forStruct(NumMatrix);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
