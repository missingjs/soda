import hash from 'object-hash';
import { Utils } from './utils';

class InternalFeature {
    constructor(private hashFn: (obj: any) => number, private equalFn: (x: any, y: any) => boolean) {
    }

    hash(obj: any): number {
        return this.hashFn(obj);
    }

    isEqual(x: any, y: any): boolean {
        return this.equalFn(x, y);
    }
}

export class ObjectFeature<T> {
    constructor(private feat: InternalFeature) {
    }

    hash(obj: T): number {
        return this.feat.hash(obj);
    }

    isEqual(x: T, y: T): boolean {
        return this.feat.isEqual(x, y);
    }
}

type FactoryType = (typ: string) => InternalFeature;

export class FeatureFactory {

    private static factoryMap = new Map<string, FactoryType>();

    static create<T>(typeName: string): ObjectFeature<T> {
        let fact = this.factoryMap.get(typeName);
        let interFeat = fact ? fact(typeName) : this.defaultFeature();
        return new ObjectFeature<T>(interFeat);
    }

    private static defaultFeature(): InternalFeature {
        return new InternalFeature(
            (obj: any) => Utils.hashCode(hash(obj)), 
            (x: any, y: any) => x === y
        );
    }

}
