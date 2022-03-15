
interface WorkInput {
    id:       number;
    args:     any[];
    expected: any;
}

export class TestWork<R> {

    private func: Function;
    private argumentTypes: string[];
    private returnType: string | undefined;
    private compareSerial = false;
    private validator: { (e: R, r: R): boolean } | undefined;
    private arguments: any[] | undefined;

    constructor(func: Function, typeHints: string[]) {
        this.func = func;
        this.argumentTypes = typeHints.slice(0, -1);
        this.returnType = typeHints.at(-1);
    }


    run(text: string): string {
        const input: WorkInput = JSON.parse(text);
        // input.
        return `processing ${text}`
    }
}
