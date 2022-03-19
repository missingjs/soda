import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import {
    PriorityQueue,
    MinPriorityQueue,
    MaxPriorityQueue
} from '@datastructures-js/priority-queue';
import { ListNode, NestedInteger } from 'soda/leetcode';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
class SummaryRanges {
    private parent: Array<number>;
    private ancestorSet: Set<number>;

    constructor() {
        this.parent = Array(10003).fill(0);
        this.ancestorSet = new Set<number>();
    }

    /** 
     * @param {number} val
     * @return {void}
     */
    addNum(val: number): void {
        ++val;
        if (this.parent[val] != 0) {
            return;
        }
        this.parent[val] = -1;
        this.ancestorSet.add(val);
        let left = val - 1;
        let right = val + 1;
        if (left > 0 && this.parent[left] != 0) {
            this.merge(left, val);
        }
        if (this.parent[right] != 0) {
            this.merge(val, right);
        }
    }

    /**
     * @return {number[][]}
     */
    getIntervals(): number[][] {
        let ans: number[] = [...this.ancestorSet];
        ans.sort((x: number, y: number) => x - y);
        let res = Array(ans.length).fill(null);
        for (let i = 0; i < res.length; ++i) {
            let start = ans[i];
            let end = start - this.parent[start] - 1;
            res[i] = [start-1, end-1];
        }
        return res;
    }

    private merge(x: number, y: number) {
        let ax = this.getAncestor(x);
        let ay = this.getAncestor(y);
        if (ax < ay) {
            this.mergeAncestor(ax, ay);
        } else {
            this.mergeAncestor(ay, ax);
        }
    }

    private mergeAncestor(ax: number, ay: number) {
        this.parent[ax] += this.parent[ay];
        this.parent[ay] = ax;
        this.ancestorSet.delete(ay);
    }

    private getAncestor(x: number): number {
        return this.parent[x] < 0
            ? x
            : (this.parent[x] = this.getAncestor(this.parent[x]));
    }
}

// step [2]: setup function/return/arguments
// let taskFunc = add;
// const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
let work = TestWork.forStruct(SummaryRanges);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
