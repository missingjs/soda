exports.TestWork = class TestWork {
    constructor(func, typeHints) {
        this.func = func;
        this.typeHints = typeHints;
        // this.argumentTypes =
    }

    run(text) {
        const input = JSON.parse(text)
        console.error(input)
        return "<" + text + ">";
    }
};
