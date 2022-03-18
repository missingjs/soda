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
/**
 * @param {NestedInteger[]} niList
 * @return {number[]}
 */
function flatNested(niList: NestedInteger[]): number[] {
    let res = [];
    let iter = new NestedIterator(niList);
    while (iter.hasNext()) {
        res.push(iter.next());
    }
    return res;
}

class Node {

    index: number = 0;

    constructor(public niList: NestedInteger[]) {
    }

    isEnd(): boolean {
        return this.index >= this.niList.length;
    }

    value(): number {
        return this.current().getInteger()!;
    }

    current(): NestedInteger {
        return this.niList[this.index]!;
    }
}

class NestedIterator {

    private stk: Node[];

    constructor(nestedList: NestedInteger[]) {
        this.stk = [new Node(nestedList)];
        this.locate();
    }

    hasNext(): boolean {
        return this.stk.length > 0 && !this.stk.at(-1).isEnd();
    }

    next(): number {
        let value = this.stk.at(-1)!.value();
        ++this.stk.at(-1)!.index;
        this.locate();
        return value;
    }

    private locate() {
        while (this.stk.length > 0) {
            if (this.stk.at(-1)!.isEnd()) {
                this.stk.pop();
                if (this.stk.length > 0) {
                    ++this.stk.at(-1)!.index;
                }
            } else if (this.stk.at(-1)!.current().isInteger()) {
                break;
            } else {
                this.stk.push(new Node(this.stk.at(-1)!.current().getList()));
            }
        }
    }

}

// step [2]: setup function/return/arguments
let taskFunc = flatNested;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
