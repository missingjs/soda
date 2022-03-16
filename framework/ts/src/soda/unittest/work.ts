import * as process from 'process';

import { ConverterFactory } from './convert';
import { FeatureFactory } from './feature';
import { Utils } from './utils';

interface WorkInput {
    id:       number;
    args:     any[];
    expected: any;
}

export class TestWork<R> {

    private func: Function;
    private argumentTypes: string[];
    private returnType: string | undefined;
    public  compareSerial = false;
    public  validator: { (e: R, r: R): boolean } | undefined;
    private arguments: any[] | undefined;

    constructor(func: Function, typeHints: string[]) {
        this.func = func;
        this.argumentTypes = typeHints.slice(0, -1);
        this.returnType = typeHints.at(-1);
    }

    static create<R>(func: Function): TestWork<R> {
        return new TestWork<R>(func, Utils.functionTypeHints(process.argv[1], func.name));
    }

    run(text: string): string {
        const input: WorkInput = JSON.parse(text);
        this.arguments = Utils.parseArguments(this.argumentTypes, input.args);

        const startTime = Utils.microTime();
        let result = this.func(...this.arguments);
        const endTime = Utils.microTime();
        const elapseMillis = (endTime - startTime) / 1000;

        let retType = this.returnType;
        if (retType?.toLowerCase() === 'void') {
            retType = this.argumentTypes[0];
            result = this.arguments[0];
        }

        const resConv = ConverterFactory.create<R>(retType!);
        const serialResult = resConv.toJsonSerializable(result);
        const resp = {
            id: input.id,
            result: serialResult,
            elapse: elapseMillis,
            success: false
        }

        let success = true;
        if (input.expected != null) {
            if (this.compareSerial && !this.validator) {
                success = JSON.stringify(input.expected) === JSON.stringify(serialResult);
            } else {
                let expect = resConv.fromJsonSerializable(input.expected);
                if (this.validator) {
                    success = this.validator(expect, result);
                } else {
                    success = FeatureFactory.create<R>(retType!).isEqual(expect, result);
                }
            }
        }
        resp.success = success;

        return JSON.stringify(resp);
    }
}
