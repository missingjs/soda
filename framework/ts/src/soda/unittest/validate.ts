import {FeatureFactory} from "./featurefactory";
import {ObjectFeature} from "./feature";
import {ListFeatureFactory} from "./listfeature";

export class Validators {
    static forArray<T>(objType: string, ordered: boolean): (x: T[], y: T[]) => boolean {
        return this.for_arr<T>(ordered, FeatureFactory.create<T>(objType));
    }

    static forArray2d<T>(
        objType: string,
        dim1Ordered: boolean,
        dim2Ordered: boolean): (x: T[][], y: T[][]) => boolean
    {
        let elemFeat = FeatureFactory.create(objType);
        let d = dim2Ordered ? ListFeatureFactory.ordered<T>(elemFeat) : ListFeatureFactory.unordered<T>(elemFeat);
        return this.for_arr(dim1Ordered, d);
    }

    static for_arr<T>(ordered: boolean, elemFeat: ObjectFeature<T>): (x: T[], y: T[]) => boolean {
        if (ordered) {
            return (x: T[], y: T[]) => ListFeatureFactory.ordered<T>(elemFeat).isEqual(x, y);
        }
        return (x: T[], y: T[]) => ListFeatureFactory.unordered<T>(elemFeat).isEqual(x, y);
    }
}