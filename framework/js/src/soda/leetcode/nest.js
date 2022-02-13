function NestedInteger(val) {

    this.isAtomic = (typeof val) === 'number';
    this.elements = [];
    this.value = val;

    // Return true if this NestedInteger holds a single integer, rather than a nested list.
    // @return {boolean}
    this.isInteger = function() {
        return this.isAtomic;
    };

    // Return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    // @return {integer}
    this.getInteger = function() {
        return this.value;
    };

    // Set this NestedInteger to hold a single integer equal to value.
    // @return {void}
    this.setInteger = function(value) {
        if (this.isInteger()) {
            this.value = value;
        }
    };

    // Set this NestedInteger to hold a nested list and adds a nested integer elem to it.
    // @return {void}
    this.add = function(elem) {
        if (!this.isInteger()) {
            this.elements.push(elem);
        }
    };

    // Return the nested list that this NestedInteger holds, if it holds a nested list
    // Return null if this NestedInteger holds a single integer
    // @return {NestedInteger[]}
    this.getList = function() {
        return [...this.elements];
    };
}

class NestedIntegerFactory {
    static parse(d) {
        if (typeof d === 'number') {
            return new NestedInteger(d);
        }
        let ni = new NestedInteger();
        d.forEach(e => ni.add(this.parse(e)));
        return ni;
    }

    static serialize(ni) {
        return ni.isInteger() ? ni.getInteger() : ni.getList().map(e => this.serialize(e));
    }

    static parseMulti(ds) {
        return ds.map(e => this.parse(e));
    }

    static serializeMulti(niList) {
        return niList.map(e => this.serialize(e));
    }
}

exports.NestedInteger = NestedInteger;
exports.NestedIntegerFactory = NestedIntegerFactory;
