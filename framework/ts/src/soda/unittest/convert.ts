export class ObjectConverter<T, JS> {

    private readonly parser: (js: JS) => T;

    private readonly serializer: (obj: T) => JS;

    constructor(parser: (js: JS) => T, serializer: (obj: T) => JS) {
        this.parser = parser;
        this.serializer = serializer;
    }

    fromJsonSerializable(js: JS): T {
        return this.parser(js);
    }

    toJsonSerializable(obj: T): JS {
        return this.serializer(obj);
    }
}

export class ConverterFactory {

    private static factoryMap = new Map<string, any>();

}
