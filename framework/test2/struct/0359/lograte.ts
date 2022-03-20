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
class Logger {
    private msgMap: Map<string, number>;
    private readonly Limit: number = 10;
    private lastTs: number;

    constructor() {
        this.msgMap = new Map<string, number>();
        this.lastTs = -this.Limit;
    }

    /** 
     * @param {number} timestamp 
     * @param {string} message
     * @return {boolean}
     */
    shouldPrintMessage(timestamp: number, message: string): boolean {
        let T = this.lastTs;
        this.lastTs = timestamp;
        if (timestamp - T >= this.Limit) {
            this.msgMap = new Map();
            this.msgMap.set(message, timestamp);
            return true;
        }

        let v = this.msgMap.get(message);
        if (typeof v === 'undefined') {
            v = timestamp - this.Limit;
        }
        if (timestamp - v < this.Limit) {
            return false;
        }
        this.msgMap.set(message, timestamp);
        return true;
    }
}

// step [2]: setup function/return/arguments
// let taskFunc = add;
// const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
let work = TestWork.forStruct(Logger);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
