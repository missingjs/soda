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
 * @param {string[]} strs
 * @return {string[][]}
 */
function groupByLength(strs: string[]): string[][] {
    shuffle(strs);
    let group = new Map<number, string[]>();
    for (let s of strs) {
        if (!group.has(s.length)) {
            group.set(s.length, []);
        }
        group.get(s.length).push(s);
    }
    let keys = [...group.keys()];
    shuffle(keys);
    return keys.map((x: number) => group.get(x));
}

function shuffle(arr: any[]) {
    for (let i = arr.length; i > 0; --i) {
        let index = Math.floor(Math.random() * i);
        if (index != i-1) {
            [arr[index], arr[i-1]] = [arr[i-1], arr[index]];
        }
    }
}

// step [2]: setup function/return/arguments
let taskFunc = groupByLength;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
work.validator = Validators.forArray2d<string>('string', false, false);
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
