const {Utils} = require('./utils');
const {ConverterFactory} = require("./convert");

class StructTester {
    static create(ctor, hintsMap) {
        return function(operations, parameters) {
            // hints of constructor has not return type
            let ctorArgs = Utils.parseArguments(hintsMap.get('constructor'), parameters[0]);
            let obj = new ctor(...ctorArgs);
            let res = [null];
            for (let i = 1; i < parameters.length; ++i) {
                let methodName = operations[i];
                if (!hintsMap.has(methodName)) {
                    throw new Error(`hints for method ${methodName} not found`);
                }
                let hints = hintsMap.get(methodName);
                let argTypes = hints.slice(0, -1);
                let retType = hints.at(-1);
                let args = Utils.parseArguments(argTypes, parameters[i]);
                let r = obj[methodName](...args);
                if (retType !== 'void' && retType !== 'Void') {
                    res.push(ConverterFactory.create(retType).toJsonSerializable(r));
                } else {
                    res.push(null);
                }
            }
            return res;
        };
    }

    static methodHints() {
        return ['string[]', 'object[]', 'object[]']
    }
}

exports.StructTester = StructTester;
