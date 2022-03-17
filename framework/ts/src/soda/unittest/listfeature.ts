import {ObjectFeature} from "./feature";
import {XMap} from './xmap';

export class ListFeatureFactory {

    static ordered<T>(elemFeat: ObjectFeature<T>): ObjectFeature<T[]> {
        let hashFn = (obj: T[]) => {
            let res = 0;
            // keep low 48 bits
            const mask = 0xffffffffffff;
            for (let e of obj) {
                let h = elemFeat.hash(e);
                res = res * 133 + h;
                res &= mask;
            }
            return res;
        };
        let equalFn = (x: T[], y: T[]) => {
            if (x.length !== y.length) {
                return false;
            }
            for (let i = 0; i < x.length; ++i) {
                if (!elemFeat.isEqual(x[i], y[i])) {
                    return false;
                }
            }
            return true;
        };
        return ObjectFeature.create<T[]>(hashFn, equalFn);
    }

    static unordered<T>(elemFeat: ObjectFeature<T>): ObjectFeature<T[]> {
        let hashFn = (obj: T[]) => {
            let res = 0;
            // keep low 48 bits
            const mask = 0xffffffffffff;
            let hashArr: number[] = obj.map(e => elemFeat.hash(e));
            hashArr.sort((x: number, y: number) => x - y);
            for (let h of hashArr) {
                res = res * 133 + h;
                res &= mask;
            }
            return res;
        };
        let equalFn = (x: T[], y: T[]) => {
            if (x.length !== y.length) {
                return false;
            }
            let xmap = new XMap<T, number>(elemFeat);
            for (let e of x) {
                xmap.set(e, xmap.get(e, 0)! + 1);
            }
            for (let e of y) {
                if (!xmap.has(e)) {
                    return false;
                }
                let c = xmap.get(e)! - 1;
                if (c === 0) {
                    xmap.delete(e);
                } else {
                    xmap.set(e, c);
                }
            }
            return true;
        };
        return ObjectFeature.create<T[]>(hashFn, equalFn);
    }

}