export class InternalFeature {
    constructor(
        private hashFn: (obj: any) => number,
        private equalFn: (x: any, y: any) => boolean) {
    }

    hash(obj: any): number {
        return this.hashFn(obj);
    }

    isEqual(x: any, y: any): boolean {
        return this.equalFn(x, y);
    }
}

export class ObjectFeature<T> {
    constructor(private feat: InternalFeature) {
    }

    hash(obj: T): number {
        return this.feat.hash(obj);
    }

    isEqual(x: T, y: T): boolean {
        return this.feat.isEqual(x, y);
    }

    internal(): InternalFeature {
        return this.feat;
    }

    static create<T>(h: (obj: T) => number, e: (x: T, y: T) => boolean) {
        return new ObjectFeature<T>(new InternalFeature(h, e));
    }
}
