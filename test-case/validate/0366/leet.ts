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
 * @param {TreeNode} root
 * @return {number[][]}
 */
function findLeaves(root: TreeNode | null): number[][] {
    let res = Array(100).fill(0).map(() => []);
    let r = solve(root, res);
    return res.slice(0, r);
}

function solve(root: TreeNode | null, res: number[][]): number {
    if (!root) {
        return 0;
    }
    let R = solve(root.right, res);
    let L = solve(root.left, res);
    let index = Math.max(L, R);
    res[index].push(root.val);
    return index + 1;
}

// step [2]: setup function/return/arguments
let taskFunc = findLeaves;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray2d<number>('number', true, false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
