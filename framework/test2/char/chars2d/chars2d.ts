import { TestWork, Utils } from 'soda/unittest';

// step [1]: implement solution function
/**
 * @param {string[][]} matrix
 * @return {string[][]}
 */
function toUpper(matrix: string[][]): string[][] {
    let diff = 'a'.charCodeAt(0) - 'A'.charCodeAt(0);
    for (let line of matrix) {
        for (let i = 0; i < line.length; ++i) {
            line[i] = String.fromCharCode(line[i].charCodeAt(0) - diff);
        }
    }
    return matrix;
}

// step [2]: setup function/return/arguments
let taskFunc = toUpper;
const work = TestWork.create<ReturnType<typeof taskFunc>>(taskFunc);
// work = TestWork.forStruct(STRUCT);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));
