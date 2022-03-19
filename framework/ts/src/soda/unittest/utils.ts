import * as fs from 'fs';
import * as process from 'process';

import { ConverterFactory } from "./convert";

export class Utils {

    static parseArguments(argTypes: string[], rawArgs: any[]): any[] {
        return argTypes.map((t, idx) => ConverterFactory.create(t).fromJsonSerializable(rawArgs[idx]))
    }

    static microTime(): number {
        let hrTime = process.hrtime();
        return hrTime[0] * 1000000 + hrTime[1] / 1000;
    }

    static hashCode(str: string): number {
        let hash = 0;
        for (let i = 0; i < str.length; i++) {
            let char = str.charCodeAt(i);
            hash = ((hash<<5)-hash)+char;
            hash = hash & hash; // Convert to 32bit integer
        }
        return hash;
    }

    static fromStdin(): string {
        return fs.readFileSync(0, 'utf-8');
    }

    static functionTypeHints(filePath: string, funcName: string): string[] {
        let lines: string[] = [];
        const allFileContents = fs.readFileSync(filePath, 'utf-8');
        let exp = new RegExp(`^function\\s+${funcName}\\s*\\(`);
        for (let line of allFileContents.split(/\r?\n/)) {
            if (exp.test(line)) {
                break;
            }
            if (line.trim().length > 0) {
                lines.push(line);
            }
        }
        // evict ' */'
        lines.pop();
        return this.parseTypeHints(lines);
    }

    private static parseTypeHints(lines: string[]): string[] {
        let retType = 'void';
        let typeHints: string[] = [];
        while (lines.length > 0) {
            let text = lines.pop();
            let m = null;
            if ((m = /^\s*\* @param\s+{(?<arg_type>.*)}/.exec(text!))) {
                typeHints.push(m.groups!.arg_type);
            } else if ((m = /^\s*\* @return[s]?\s+{(?<return_type>.*)}/.exec(text!))) {
                retType = m.groups!.return_type;
            } else {
                break;
            }
        }
        typeHints.reverse().push(retType);
        return typeHints;
    }

    static methodTypeHints(filePath: string, className: string): Map<string, string[]> {
        let hintsMap = new Map<string, string[]>();
        let match: RegExpMatchArray | null = null;
        // @ts-ignore
        for (let _d of this.parseMethodDesc(filePath, className)) {
            let desc = _d as FunctionDesc;
            if (/^\s*constructor\s*[(]/.test(desc.declare)) {
                hintsMap.set('constructor', desc.typeHints);
            } else if ((match = /^\s*(?<methodName>\w+)\s*[(]/.exec(desc.declare))) {
                hintsMap.set(match.groups!.methodName, desc.typeHints);
            }
        }
        if (hintsMap.has('constructor')) {
            // hints of constructor has not return type
            hintsMap.get('constructor')!.pop();
        } else {
            hintsMap.set('constructor', []);
        }
        return hintsMap;
    }

    private static * parseMethodDesc(filePath: string, className: string) {
        let lines = fs.readFileSync(filePath, 'utf-8').split(/\r?\n/);
        let index = 0;
        let classLine = new RegExp(`^\\s*class\\s+${className}`);
        while (!classLine.test(lines[index])) {
            ++index;
        }

        while (index < lines.length) {
            let line = lines[index++];
            if (/^\s*\/\*\*/.test(line)) {
                let buf: string[] = [];
                while (index < lines.length && !/^\s*\*\//.test(lines[index])) {
                    buf.push(lines[index++]);
                }
                if (index == lines.length) {
                    throw new Error('Invalid method description comment');
                }
                ++index;  // skip last line of comments: */
                let typeHints = this.parseTypeHints(buf);
                let methodDef = lines[index++];
                yield {
                    typeHints: typeHints,
                    declare: methodDef
                }
            } else if (/^[}]/.test(line)) {
                break;
            }
        }
    }

}

interface FunctionDesc {
    typeHints: string[],
    declare:   string
}