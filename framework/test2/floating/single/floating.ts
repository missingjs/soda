import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {number} a
 * @param {number} b
 * @return {number}
 */
function divide(a: number, b: number): number {
    return a / b;
}

// step [2]: setup function/return/arguments
let taskFunc = divide;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
// work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
