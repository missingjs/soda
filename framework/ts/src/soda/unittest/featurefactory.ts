import hash from "object-hash";

import {InternalFeature, ObjectFeature} from "./feature";
import {ListFeatureFactory} from "./listfeature";
import {Utils} from "./utils";

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

    private static registerFactory(typeName: string, factory: FactoryType) {
        this.factoryMap.set(typeName, factory);
    }

    static {
        this.registerFactory('number', () => {
            return new InternalFeature(
                (obj: number) => Utils.hashCode(hash(obj)),
                (x: number, y: number) => Math.abs(x - y) < 1e-6
            );
        });
        this.registerFactory('number[]', () => {
            return ListFeatureFactory.ordered(this.create('number')).internal()
        });
        this.registerFactory('number[][]', () => {
            return ListFeatureFactory.ordered(this.create('number[]')).internal()
        });
    }

}
