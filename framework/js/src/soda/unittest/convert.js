const {ListFactory} = require('../leetcode/list');
const {NestedIntegerFactory} = require('../leetcode/nest');

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
}

class ConverterFactory {
    static factoryMap = new Map();

    static create(type) {
        if (this.factoryMap.has(type)) {
            return this.factoryMap.get(type)()
        }
        return new ObjectConverter(j => j, o => o);
    }

    static registerFactory(type, {factory, parser, serializer} = {}) {
        if (factory) {
            this.factoryMap.set(type, factory);
        } else {
            this.factoryMap.set(type, () => new ObjectConverter(parser, serializer));
        }
    }

    static {
        this.registerFactory('ListNode', {
            parser: ListFactory.create,
            serializer: ListFactory.dump
        });
        this.registerFactory('ListNode[]', {
            parser: ls => ls.map(e => ListFactory.create(e)),
            serializer: ts => ts.map(e => ListFactory.dump(e))
        });
        this.registerFactory('NestedInteger', {
            parser: d => NestedIntegerFactory.parse(d),
            serializer: n => NestedIntegerFactory.serialize(n)
        });
        this.registerFactory('NestedInteger[]', {
            parser: ds => NestedIntegerFactory.parseMulti(ds),
            serializer: ns => NestedIntegerFactory.serializeMulti(ns)
        });
    }
}

exports.ConverterFactory = ConverterFactory;
exports.ObjectConverter = ObjectConverter;
