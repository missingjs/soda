import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {number[][]} a
 * @param {number[][]} b
 * @return {number[][]}
 */
function matrixMultiply(a: number[][], b: number[][]): number[][] {
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
}

// step [2]: setup function/return/arguments
let taskFunc = matrixMultiply;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
// work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
