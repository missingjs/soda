const process = require('process');

const {ConverterFactory} = require("./convert");
const {FeatureFactory} = require("./featurefactory");
const {Utils} = require('./utils');

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

    static create(func) {
        return new TestWork(func, Utils.functionTypeHints(process.argv[1], func.name));
    }

    run(text) {
        const input = JSON.parse(text);  // {id: 1, expected: object, args: [...]}
        this.arguments = Utils.parseArguments(this.argumentTypes, input.args);

        const startTime = Utils.microTime();
        let result = this.func(...this.arguments);
        const endTime = Utils.microTime();
        const elapseMillis = (endTime - startTime) / 1000;

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
                success = JSON.stringify(input.expected) === JSON.stringify(serialResult);
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

        return JSON.stringify(resp);
    }
}

exports.TestWork = TestWork;
