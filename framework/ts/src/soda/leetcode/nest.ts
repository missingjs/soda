export class NestedInteger {

    private isAtomic: boolean;
    private elements: NestedInteger[] = [];
    private value: number;

    // If value is provided, then it holds a single integer
    // Otherwise it holds an empty nested list
    constructor(value?: number) {
        if (value != null) {
            this.isAtomic = true;
            this.value = value;
        } else {
            this.isAtomic = false;
            this.value = 0;
        }
    }

    // Return true if this NestedInteger holds a single integer, rather than a nested list.
    isInteger(): boolean {
        return this.isAtomic;
    }

    // Return the single integer that this NestedInteger holds, if it holds a single integer
    // Return null if this NestedInteger holds a nested list
    getInteger(): number | null {
        return this.value;
    }

    // Set this NestedInteger to hold a single integer equal to value.
    setInteger(value: number) {
        if (this.isInteger()) {
            this.value = value;
        }
    }

    // Set this NestedInteger to hold a nested list and adds a nested integer elem to it.
    add(elem: NestedInteger) {
        if (!this.isInteger()) {
            this.elements.push(elem);
        }
    }

    // Return the nested list that this NestedInteger holds,
    // or an empty list if this NestedInteger holds a single integer
    getList(): NestedInteger[] {
        return [...this.elements];
    }
}

export class NestedIntegerFactory {
    static parse(d: any): NestedInteger {
        if (typeof d === 'number') {
            return new NestedInteger(d as number);
        }
        let ni = new NestedInteger();
        d.forEach((e: any) => ni.add(this.parse(e)));
        return ni;
    }

    static serialize(ni: NestedInteger): any {
        return ni.isInteger()
            ? ni.getInteger()
            : ni.getList().map((e: NestedInteger) => this.serialize(e));
    }

    static parseMulti(ds: any[]): NestedInteger[] {
        return ds.map(e => this.parse(e));
    }

    static serializeMulti(niList: NestedInteger[]): any[] {
        return niList.map(e => this.serialize(e));
    }
}
