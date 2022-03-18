import {ListNode, ListFactory as LinkedListFactory} from '../leetcode/list';
import {NestedInteger, NestedIntegerFactory} from "../leetcode/nest";

class InternalConverter {

    constructor(private parser: (js: any) => any, private serializer: (obj: any) => any) {
    }

    fromJsonSerializer(js: any): any {
        return this.parser(js);
    }

    toJsonSerializer(obj: any): any {
        return this.serializer(obj);
    }

}

export class ObjectConverter<T> {

    constructor(private converter: InternalConverter) {
    }

    fromJsonSerializable(js: any): T {
        return this.converter.fromJsonSerializer(js) as T;
    }

    toJsonSerializable(obj: T): any {
        return this.converter.toJsonSerializer(obj);
    }

    internal(): InternalConverter {
        return this.converter;
    }
}

type FactoryType = (typ: string) => InternalConverter;
type ParserFunc<T> = (js: any) => T;
type SerialFunc<T> = (obj: T) => any;

export class ConverterFactory {

    private static factoryMap = new Map<string, FactoryType>();

    static create<T>(typeName: string): ObjectConverter<T> {
        let fact = this.factoryMap.get(typeName);
        let ic = fact ? fact(typeName) : new InternalConverter(j => j, o => o);
        return new ObjectConverter<T>(ic);
    }

    private static registerFactory(typeName: string, factory: FactoryType) {
        this.factoryMap.set(typeName, factory);
    }

    private static registerByFunc<T>(typeName: string, parser: ParserFunc<T>, serializer: SerialFunc<T>) {
        this.registerFactory(typeName, () => new InternalConverter(parser, serializer));
    }

    static {
        this.registerByFunc(
            'ListNode', 
            LinkedListFactory.create, 
            LinkedListFactory.dump
        );
        this.registerByFunc(
            'ListNode[]',
            (ls: number[][]) => ls.map((e: number[]) => LinkedListFactory.create(e)!),
            (ts: ListNode[]) => ts.map(e => LinkedListFactory.dump(e)),
        );
        this.registerByFunc(
            'NestedInteger',
            (d: any) => NestedIntegerFactory.parse(d),
            (n: NestedInteger) => NestedIntegerFactory.serialize(n)
        );
        this.registerByFunc(
            'NestedInteger[]',
            (ds: any[]) => NestedIntegerFactory.parseMulti(ds),
            (ns: NestedInteger[]) => NestedIntegerFactory.serializeMulti(ns)
        );
    }

}
