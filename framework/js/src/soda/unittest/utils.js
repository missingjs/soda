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

    static methodTypeHints(filePath, ctorName) {
        const allFileContents = fs.readFileSync(filePath, 'utf-8');
        let m = null;
        let hintsMap = new Map();
        for (let desc of parseFunctionDesc(allFileContents)) {
            if (desc.declare.match('^var ' + ctorName + ' = function')) {
                hintsMap.set('constructor', desc.typeHints);
            } else if ((m = desc.declare.match(`^${ctorName}[.]prototype[.](\\w+)`))) {
                hintsMap.set(m[1], desc.typeHints);
            }
        }
        if (hintsMap.has('constructor')) {
            // hints of constructor has not return type
            hintsMap.get('constructor').pop();
        } else {
            hintsMap.set('constructor', []);
        }
        return hintsMap;
    }

    static functionTypeHints(filePath, funcName) {
        let lines = [];
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
        return parseTypeHints(lines);
    }

    static fromStdin() {
        return fs.readFileSync(0, "utf-8");
    }

    static microTime() {
        let hrTime = process.hrtime()
        return hrTime[0] * 1000000 + hrTime[1] / 1000;
    }
}

function* parseFunctionDesc(content) {
    let buf = [];
    let lines = content.split(/\r?\n/);
    for (let i = 0; i < lines.length; ++i) {
        const line = lines[i];
        if (line.match(/^\/\*\*/)) {
            ++i;
            while (!lines[i].match(/^ \*\//)) {
                buf.push(lines[i++]);
            }
            ++i;
            let typeHints = parseTypeHints(buf);
            yield {
                typeHints: typeHints,
                declare: lines[i]
            }
        }
    }
}

function parseTypeHints(lines) {
    let retType = 'void';
    let typeHints = [];
    while (lines.length > 0) {
        let text = lines.pop();
        let m = null;
        if ((m = /^ \* @param\s+{(?<arg_type>.*)}/.exec(text))) {
            typeHints.push(m.groups.arg_type);
        } else if ((m = /^ \* @return[s]?\s+{(?<return_type>.*)}/.exec(text))) {
            retType = m.groups.return_type;
        } else {
            break;
        }
    }
    typeHints.reverse().push(retType);
    return typeHints;
}

exports.Utils = Utils;
