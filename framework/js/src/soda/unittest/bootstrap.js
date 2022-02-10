const {TestWork} = require('soda/unittest');

var add = function(a, b) {
    return a + b;
};

const work = new TestWork(add, ['number', 'number', 'number']);
work.compareSerial = true;
const fs = require("fs");
const data = fs.readFileSync(0, "utf-8");
console.log(work.run(data));
