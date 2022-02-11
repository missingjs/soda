class XEntry {
    constructor(key, value) {
        this.key = key;
        this.value = value;
    }
}

class XMap {
    constructor(feature) {
        this.feat = feature;
        this.dict = new Map();
        this.size = 0;
    }

    _hash(key) {
        return this.feat.hash(key);
    }

    _entry(key) {
        let h = this._hash(key);
        if (!this.dict.has(h)) {
            return null;
        }
        let e = this.dict.get(h).find(x => this.feat.isEqual(key, x.key));
        return e || null;
    }

    has(key) {
        return this._entry(key) != null;
    }

    get(key, defaultVal = null) {
        let e = this._entry(key);
        return e != null ? e.value : defaultVal;
    }

    set(key, value) {
        let e = this._entry(key);
        if (e != null) {
            e.value = value;
        } else {
            let h = this._hash(key);
            if (!this.dict.has(h)) {
                this.dict.set(h, []);
            }
            this.dict.get(h).push(new XEntry(key, value));
            ++this.size;
        }
    }

    delete(key) {
        let h = this._hash(key);
        if (!this.dict.has(h)) {
            return;
        }
        let index = this.dict.get(h).findIndex(x => this.feat.isEqual(key, x.key));
        if (index >= 0) {
            this.dict.get(h).splice(index, 1);
            --this.size;
        }
    }
}

class UnorderedListFeature {
    constructor(elemFeat) {
        this.elemFeat = elemFeat;
    }

    hash(obj) {
        let res = 0;
        // keep low 48 bits
        const mask = 0xffffffffffff;
        let hashArr = obj.map(e => this.elemFeat.hash(e));
        hashArr.sort((x, y) => x - y);
        for (let h of hashArr) {
            res = res * 133 + h;
            res &= mask;
        }
        return res;
    }

    isEqual(x, y) {
        if (x.length !== y.length) {
            return false;
        }
        let xmap = new XMap(this.elemFeat);
        for (let e of x) {
            xmap.set(e, xmap.get(e, 0) + 1);
        }
        for (let e of y) {
            if (!xmap.has(e)) {
                return false;
            }
            let c = xmap.get(e) - 1;
            if (c === 0) {
                xmap.delete(e);
            } else {
                xmap.set(e, c);
            }
        }
        return true;
    }
}

exports.UnorderedListFeature = UnorderedListFeature;
