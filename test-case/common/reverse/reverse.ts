import {
    AvlTree,
    BinarySearchTree
} from '@datastructures-js/binary-search-tree';
import { TestWork, Utils, Validators } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {string} s
 * @return {string}
 */
function reverseVowels(s: string): string {
    let isv = new Array(128).fill(false);
    for (let ch of "aeiouAEIOU") {
        isv[ch.charCodeAt(0)] = true;
    }
    let buf = Array.from<string>(s);
    let [i, j] = [0, s.length-1];
    while (i < j) {
        while (i < j && !isv[buf[i].charCodeAt(0)]) {
            ++i;
        }
        while (i < j && !isv[buf[j].charCodeAt(0)]) {
            --j;
        }
        if (i < j) {
            [buf[i], buf[j]] = [buf[j], buf[i]];
            ++i;
            --j;
        }
    }
    return buf.join('');
}

// step [2]: setup function/return/arguments
let taskFunc = reverseVowels;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
