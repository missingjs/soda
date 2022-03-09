package soda.groovy.unittest.validate

class XMap<K, V> {

    static class XEntry<K, V> {
        K key
        V value
    }

    private ObjectFeature<K> feat

    private Map<Long, List<XEntry<K, V>>> dict = new HashMap<>()

    private int size

    XMap(ObjectFeature<K> feat) {
        this.feat = feat
    }

    private long _hash(K key) {
        feat.hash(key)
    }

    private XEntry<K, V> _entry(K key) {
        long h = _hash(key)
        if (h in dict) {
            for (entry in dict[h]) {
                if (feat.isEqual(key, entry.key)) {
                    return entry
                }
            }
        }
        null
    }

    boolean isCase(K key) {
        containsKey(key)
    }

    boolean containsKey(K key) {
        _entry(key) != null
    }

    V getAt(K key) {
        _entry(key)?.value
    }

    V getOrDefault(K key, V defaultValue) {
        getAt(key) ?: defaultValue
    }

    void putAt(K key, V value) {
        def e = _entry(key)
        if ( e == null) {
            long h = _hash(key)
            def values = dict.computeIfAbsent(h, k -> new ArrayList<>())
            e = new XEntry<K, V>(key: key, value: value)
            values.add(e)
            ++size
        }
        e.value = value
    }

    void remove(K key) {
        long h = _hash(key)
        if (!dict.containsKey(h)) {
            return
        }
        XEntry<K, V> e = null
        for (entry in dict[h]) {
            if (feat.isEqual(key, entry.key)) {
                e = entry
                break
            }
        }
        if (e != null) {
            dict.get(h).remove(e)
            --size
        }
    }

    int getSize() {
        size
    }

}
