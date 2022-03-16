
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
        this.converter = converter;
    }

    fromJsonSerializable(js: any): T {
        return this.converter.fromJsonSerializer(js) as T;
    }

    toJsonSerializable(obj: T): any {
        return this.converter.toJsonSerializer(obj);
    }
}

type FactoryType = (typ: string) => InternalConverter;

export class ConverterFactory {

    private static factoryMap = new Map<string, FactoryType>();

    static create<T>(typeName: string): ObjectConverter<T> {
        let fact = this.factoryMap.get(typeName);
        let ic = fact ? fact(typeName) : new InternalConverter(j => j, o => o);
        return new ObjectConverter<T>(ic);
    }

}
