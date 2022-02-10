class ObjectFeature {
    constructor(hashFunc, equalFunc) {
        this.hashFunc = hashFunc;
        this.equalFunc = equalFunc;
    }

    hash(obj) {
        return this.hashFunc(obj);
    }

    isEqual(x, y) {
        return this.equalFunc(x, y)
    }
}

exports.ObjectFeature = ObjectFeature;
