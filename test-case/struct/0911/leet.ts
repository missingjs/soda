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
class TopVotedCandidate {
    private N: number;
    private times: number[];
    private winner: number[];

    /**
     * @param {number[]} persons
     * @param {number[]} times
     */
    constructor(persons: number[], times: number[]) {
        this.N = persons.length;
        this.times = times;
        this.winner = Array(this.N).fill(0);
        let counter = Array(this.N+1).fill(0);
        let win = 0;
        for (let i = 0; i < this.N; ++i) {
            ++counter[persons[i]];
            if (counter[persons[i]] >= counter[win]) {
                win = persons[i];
            }
            this.winner[i] = win;
        }
    }

    /** 
     * @param {number} t
     * @return {number}
     */
    q(t: number): number {
        if (t >= this.times.at(-1)) {
            return this.winner[this.N-1];
        }
        let [low, high] = [0, this.N-1];
        while (low < high) {
            let mid = Math.floor((low + high) / 2);
            if (t <= this.times[mid]) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return t == this.times[low]
            ? this.winner[low]
            : this.winner[low-1];
    }
}

// step [2]: setup function/return/arguments
// let taskFunc = add;
// const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
let work = TestWork.forStruct(TopVotedCandidate);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
