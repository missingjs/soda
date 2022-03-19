import {Utils} from "./utils";
import {ConverterFactory} from "./convert";

interface StructConstructor {
    new (...args: any[]): any;
}

export class StructTester {
    static create(ctorFunc: Function, hintsMap: Map<string, string[]>): (ops: string[], params: any[]) => any[] {
        return (operations: string[], parameters: any[]) => {
            // hints of constructor has not return type
            let ctorArgs = Utils.parseArguments(hintsMap.get('constructor')!, parameters[0]);
            let ctor = ctorFunc as StructConstructor;
            // @ts-ignore
            let obj = new ctor(...ctorArgs);
            let res = [null];
            for (let i = 1; i < parameters.length; ++i) {
                let methodName = operations[i];
                if (!hintsMap.has(methodName)) {
                    throw new Error(`hints for method ${methodName} not found`);
                }
                let hints = hintsMap.get(methodName)!;
                let argTypes = hints.slice(0, -1);
                let retType = hints.at(-1)!;
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

    static methodHints(): string[] {
        return ['string[]', 'object[]', 'object[]']
    }
}