import { TestWork, Utils } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {string} ch
 * @return {string}
 */
function nextChar(ch: string): string {
    return String.fromCharCode(ch.charCodeAt(0) + 1);
}

// step [2]: setup function/return/arguments
let taskFunc = nextChar;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

