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

exports.FeatureFactory = FeatureFactory;
