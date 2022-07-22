const {ListNode, NestedInteger} = require('soda/leetcode');
const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {string} s
 * @return {NestedInteger}
 */
var deserialize = function(s) {
    p = 0;
    return parse(s);
};

let p = 0;

function parse(s) {
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

const work = TestWork.create(deserialize);
// work.validator = (x, y) => { ... };
work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

