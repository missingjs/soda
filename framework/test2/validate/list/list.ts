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
/**
 * @param {character[]} chars
 * @param {number} n
 * @return {character[]}
 */
function permutation(chars: string[], n: number): string[] {
    let res = [];
    let buf = Array(n).fill(null);
    solve(chars, 0, buf, 0, res);
    return res;
}

function solve(chars: string[], i: number, buf: string[], j: number, res: string[]) {
    if (j == buf.length) {
        res.push(buf.join(''));
        return;
    }
    for (let k = i; k < chars.length; ++k) {
        [chars[i], chars[k]] = [chars[k], chars[i]];
        buf[j] = chars[i];
        solve(chars, i+1, buf, j+1, res);
        [chars[i], chars[k]] = [chars[k], chars[i]];
    }
}

// step [2]: setup function/return/arguments
let taskFunc = permutation;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray<string>('string', false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
