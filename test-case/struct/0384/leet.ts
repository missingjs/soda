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
class Solution {
    private original: number[];
    /**
     * @param {number[]} nums
     */
    constructor(nums: number[]) {
        this.original = nums;
    }

    /**
     * @return {number[]}
     */
    reset(): number[] {
        return [...this.original];
    }

    /**
     * @return {number[]}
     */
    shuffle(): number[] {
        let res = this.reset();
        for (let s = res.length; s > 0; --s) {
            let i = Math.floor(Math.random() * s);
            let j = s - 1;
            if (i != j) {
                [res[i], res[j]] = [res[j], res[i]];
            }
        }
        return res;
    }
}

// step [2]: setup function/return/arguments
// let taskFunc = add;
// const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
let work = TestWork.forStruct(Solution);
work.validator = (expect: number[], result: number[]) => {
    let args = work.arguments;
    let commands = args[0];
    let arrCmp = Validators.forArray<number>('number', false);
    for (let i = 1; i < commands.length; ++i) {
        let cmd = commands[i];
        if (cmd === 'shuffle') {
            let evalues = expect[i];
            let rvalues = result[i];
            if (!arrCmp(evalues, rvalues)) {
                return false;
            }
        }
    }
    return true;
};
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
