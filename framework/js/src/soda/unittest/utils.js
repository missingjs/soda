const {ConverterFactory} = require('./convert')

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
}

exports.Utils = Utils;
