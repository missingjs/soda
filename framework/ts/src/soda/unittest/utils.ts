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
        for (let line of allFileContents.split(/\r?\n/)) {
            let exp = new RegExp(`^function\\s+${funcName}\\s*\\(`);
            if (line.match(exp)) {
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
            if ((m = /^ \* @param\s+{(?<arg_type>.*)}/.exec(text!))) {
                typeHints.push(m.groups!.arg_type);
            } else if ((m = /^ \* @return[s]?\s+{(?<return_type>.*)}/.exec(text!))) {
                retType = m.groups!.return_type;
            } else {
                break;
            }
        }
        typeHints.reverse().push(retType);
        return typeHints;
    }

}