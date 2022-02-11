const hash = require('object-hash');
const {ObjectFeature} = require("./feature");
const {Utils} = require("./utils");

class FeatureFactory {
    static factoryMap = new Map();

    static create(type) {
        if (this.factoryMap.has(type)) {
            return this.factoryMap.get(type)();
        }
        return new ObjectFeature(a => Utils.hashCode(hash(a)), (x, y) => x === y);
    }
}

class ListFeature {
    constructor(elemFeat) {
        this.elemFeat = elemFeat;
    }

    hash(obj) {
        let res = 0;
        // keep low 48 bits
        const mask = 0xffffffffffff;
        for (let e of obj) {
            let h = this.elemFeat.hash(e);
            res = res * 133 + h;
            res &= mask;
        }
        return res;
    }

    isEqual(x, y) {
        if (x.length !== y.length) {
            return false;
        }
        for (let i = 0; i < x.length; ++i) {
            if (!this.elemFeat.isEqual(x[i], y[i])) {
                return false;
            }
        }
        return true;
    }
}

exports.FeatureFactory = FeatureFactory;
