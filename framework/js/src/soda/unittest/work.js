const {Utils} = require('./utils')
const {ConverterFactory} = require("./convert");
const {FeatureFactory} = require("./featurefactory");
const hash = require('object-hash');

class TestWork {
    constructor(func, typeHints) {
        this.func = func;
        this.typeHints = typeHints;
        this.argumentTypes = typeHints.slice(0, -1);
        this.returnType = typeHints[-1];
        this.compareSerial = false;
        this.validator = null;
        this.arguments = null;
    }

    run(text) {
        const input = JSON.parse(text);  // {id: 1, expected: object, args: [...]}
        console.error(input);
        this.arguments = Utils.parseArguments(this.argumentTypes, input.args);

        const startTime = Date.now();
        let result = this.func(...this.arguments);
        const endTime = Date.now();
        const elapseMillis = endTime - startTime;

        let retType = this.returnType;
        if (retType === 'Void' || retType === 'void') {
            retType = this.argumentTypes[0];
            result = this.arguments[0];
        }

        const resConv = ConverterFactory.create(retType);
        const serialResult = resConv.toJsonSerializable(result);
        const resp = {
            id: input.id,
            result: serialResult,
            elapse: elapseMillis
        };

        let success = true;
        if (input.expected != null) {
            if (this.compareSerial && this.validator == null) {
                success = input.expected === serialResult;
            } else {
                let expect = resConv.fromJsonSerializable(input.expected);
                if (this.validator != null) {
                    success = this.validator(expect, result);
                } else {
                    success = FeatureFactory.create(retType).isEqual(expect, result);
                }
            }
        }
        resp.success = success;

        console.error(hash(resp));

        return JSON.stringify(resp);
    }
}

exports.TestWork = TestWork;
