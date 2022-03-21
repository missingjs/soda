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
 * @param {string[]} s
 * @return {void}
 */
function reverseWords(s: string[]): void {
    if (s.length == 0) {
        return;
    }
    reverse(s, 0, s.length-1);
    let N = s.length;
    let [i, j] = [0, 0];
    while (j < N) {
        if (s[j] == ' ') {
            reverse(s, i, j-1);
            i = j + 1;
        }
        ++j;
    }
    if (i < j) {
        reverse(s, i, j-1);
    }
}

function reverse(s: string[], i: number, j: number) {
    while (i < j) {
        [s[i], s[j]] = [s[j], s[i]];
        ++i;
        --j;
    }
}

// step [2]: setup function/return/arguments
let taskFunc = reverseWords;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// let work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
