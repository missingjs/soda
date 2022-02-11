const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {string} s
 * @return {string}
 */
var reverseVowels = function(s) {
    let isv = new Array(128).fill(false);
    for (let ch of "aeiouAEIOU") {
        isv[ch.charCodeAt(0)] = true;
    }
    let buf = Array.from(s);
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
};

const work = TestWork.create(reverseVowels);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

