const {FeatureFactory, ListFeature} = require('./featurefactory');
const {UnorderedListFeature} = require('./unordered');

class Validators {
    static forArray(objType, ordered) {
        return this.for_arr(ordered, FeatureFactory.create(objType));
    }

    static forArray2d(objType, dim1Ordered, dim2Ordered) {
        let elemFeat = FeatureFactory.create(objType);
        let d = dim2Ordered ? new ListFeature(elemFeat) : new UnorderedListFeature(elemFeat);
        return this.for_arr(dim1Ordered, d);
    }

    static for_arr(ordered, elemFeat) {
        if (ordered) {
            return (x, y) => new ListFeature(elemFeat).isEqual(x, y);
        }
        return (x, y) => new UnorderedListFeature(elemFeat).isEqual(x, y);
    }
}

exports.Validators = Validators;
