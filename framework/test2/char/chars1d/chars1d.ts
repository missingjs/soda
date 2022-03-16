import { TestWork, Utils } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {string[]} chars
 * @return {string[]}
 */
function doubleList(chars: string[]): string[] {
    return chars.concat(chars);
}

// step [2]: setup function/return/arguments
let taskFunc = doubleList;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
