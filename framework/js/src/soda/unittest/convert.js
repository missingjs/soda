class ObjectConverter {
    constructor(parser, serializer) {
        this.parser = parser;
        this.serializer = serializer;
    }

    fromJsonSerializable(js) {
        return this.parser(js);
    }

    toJsonSerializable(obj) {
        return this.serializer(obj);
    }
};

class ConverterFactory {
    static factoryMap = new Map();
    static {
        // TODO
    }

    static create(type) {
        if (this.factoryMap.has(type)) {
            return this.factoryMap.get(type)()
        }
        return new ObjectConverter(j => j, o => o);
    }
}

exports.ConverterFactory = ConverterFactory;
exports.ObjectConverter = ObjectConverter;
