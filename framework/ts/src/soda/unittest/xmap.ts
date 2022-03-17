import { ObjectFeature } from "./feature";

class XEntry<K, V> {
    constructor(public key: K, public value: V) {
    }
}

export class XMap<K, V> {

    private feat: ObjectFeature<K>;
    private dict: Map<number, XEntry<K, V>[]>;
    private size: number;

    constructor(feature: ObjectFeature<K>) {
        this.feat = feature;
        this.dict = new Map<number, XEntry<K, V>[]>();
        this.size = 0;
    }

    _hash(key: K): number {
        return this.feat.hash(key);
    }

    _entry(key: K) {
        let h = this._hash(key);
        if (!this.dict.has(h)) {
            return null;
        }
        let e = this.dict.get(h)!.find(x => this.feat.isEqual(key, x.key));
        return e || null;
    }

    has(key: K): boolean {
        return this._entry(key) != null;
    }

    get(key: K, defaultVal: V | null = null): V | null {
        let e = this._entry(key);
        return e != null ? e.value : defaultVal;
    }

    set(key: K, value: V) {
        let e = this._entry(key);
        if (e != null) {
            e.value = value;
        } else {
            let h = this._hash(key);
            if (!this.dict.has(h)) {
                this.dict.set(h, []);
            }
            this.dict.get(h)!.push(new XEntry(key, value));
            ++this.size;
        }
    }

    delete(key: K) {
        let h = this._hash(key);
        if (!this.dict.has(h)) {
            return;
        }
        let index = this.dict.get(h)!.findIndex(x => this.feat.isEqual(key, x.key));
        if (index >= 0) {
            this.dict.get(h)!.splice(index, 1);
            --this.size;
        }
    }
}
