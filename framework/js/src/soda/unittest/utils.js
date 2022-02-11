const fs = require("fs");
const process = require("process");

const {ConverterFactory} = require('./convert');

class Utils {
    static parseArguments(argTypes, rawArgs) {
        return argTypes.map((t, idx) => ConverterFactory.create(t).fromJsonSerializable(rawArgs[idx]))
    }

    static hashCode(str) {
        let hash = 0;
        for (let i = 0; i < str.length; i++) {
            let char = str.charCodeAt(i);
            hash = ((hash<<5)-hash)+char;
            hash = hash & hash; // Convert to 32bit integer
        }
        return hash;
    }

    static functionTypeHints(filePath, funcName) {
        let lines = [];
        const fs = require('fs');
        const allFileContents = fs.readFileSync(filePath, 'utf-8');
        for (let line of allFileContents.split(/\r?\n/)) {
            let exp = new RegExp(`^var\\s+${funcName}\\s+=\\s+function\\s*\\(`);
            if (line.match(exp)) {
                break;
            }
            if (line.trim().length > 0) {
                lines.push(line);
            }
        }
        // evict ' */'
        lines.pop();

        let retType = 'void';
        let argTypes = [];
        while (lines.length > 0) {
            let text = lines.pop();
            let m = null;
            if ((m = /^ \* @param\s+{(?<arg_type>.*)}/.exec(text))) {
                argTypes.push(m.groups.arg_type);
            } else if ((m = /^ \* @return[s]?\s+{(?<return_type>.*)}/.exec(text))) {
                retType = m.groups.return_type;
            } else {
                break;
            }
        }
        argTypes.push(retType);
        return argTypes;
    }

    static fromStdin() {
        return fs.readFileSync(0, "utf-8");
    }

    static microTime() {
        let hrTime = process.hrtime()
        return hrTime[0] * 1000000 + hrTime[1] / 1000;
    }
}

exports.Utils = Utils;
