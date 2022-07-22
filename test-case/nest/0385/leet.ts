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
 * @param {string} s
 * @return {NestedInteger}
 */
function deserialize(s: string): NestedInteger {
    p = 0;
    return parse(s);
}

let p = 0;

function parse(s: string): NestedInteger {
    if (s[p] === '[') {
        ++p;
        let root = new NestedInteger();
        while (s[p] !== ']') {
            root.add(parse(s));
            if (s[p] === ',') {
                ++p;
            }
        }
        ++p;
        return root;
    }

    let negative = false;
    if (s[p] === '-') {
        ++p;
        negative = true;
    }

    let value = 0;
    while (p < s.length && s[p] >= '0' && s[p] <= '9') {
        value = value * 10 + s.charCodeAt(p) - '0'.charCodeAt(0);
        ++p;
    }

    if (negative) {
        value = -value;
    }
    return new NestedInteger(value);
}

// step [2]: setup function/return/arguments
let taskFunc = deserialize;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
