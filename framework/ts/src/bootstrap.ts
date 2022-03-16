import { TestWork, Utils } from './soda/unittest';

// step [1]: implement solution function
/**
 * @param {number} a
 * @param {number} b
 * @return {number}
 */
function add(a: number, b: number): number {
    return a + b;
}

// step [2]: setup function/return/arguments
let taskFunc = add;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
