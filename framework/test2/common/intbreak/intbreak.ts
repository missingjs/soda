import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import { TestWork, Utils } from 'soda/unittest';

// step [1]: implement solution function
let memo: number[] = [];
/**
 * @param {number} n
 * @return {number}
 */
function integerBreak(n: number): number {
    memo = new Array<number>(59).fill(0);
    return solve(n);
}

function solve(n: number): number {
    if (n == 1) {
        return 1;
    }
    if (memo[n] > 0) {
        return memo[n];
    }
    let res = 0;
    for (let i = 1; i < n; ++i) {
        res = Math.max(i * (n-i), i * solve(n-i), res);
    }
    memo[n] = res;
    return res;
}

// step [2]: setup function/return/arguments
let taskFunc = integerBreak;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
