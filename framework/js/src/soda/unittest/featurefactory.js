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

    static registerFactory(type, factory) {
        this.factoryMap.set(type, factory);
    }

    static {
        this.registerFactory('number', () => new NumberFeature());
        this.registerFactory('number[]', () => new ListFeature(this.create('number')));
        this.registerFactory('number[][]', () => new ListFeature(this.create('number[]')));
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

class NumberFeature {
    hash(obj) {
        return Utils.hashCode(hash(obj));
    }

    isEqual(x, y) {
        return Math.abs(x - y) < 1e-6;
    }
}

exports.FeatureFactory = FeatureFactory;
