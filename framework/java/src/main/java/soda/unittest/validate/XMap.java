package soda.unittest.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMap<K, V> {

    public static class XEntry<K, V> {
        public final K key;
        public V value;
        public XEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private ObjectFeature<K> feat;

    private Map<Long, List<XEntry<K,V>>> dict = new HashMap<>();

    private int size;

    public XMap(ObjectFeature<K> feat) {
        this.feat = feat;
    }

    private long _hash(K key) {
        return feat.hash(key);
    }

    public boolean contains(K key) {
        var e = _entry(key);
        return e != null;
    }

    public V get(K key) {
        var e = _entry(key);
        return e != null ? e.value : null;
    }

    public V getOrDefault(K key, V defaultValue) {
        V v = get(key);
        return v != null ? v : defaultValue;
    }

    private XEntry<K, V> _entry(K key) {
        long h = _hash(key);
        if (dict.containsKey(h)) {
            for (var entry : dict.get(h)) {
                if (feat.isEqual(key, entry.key)) {
                    return entry;
                }
            }
        }
        return null;
    }

    public void put(K key, V value) {
        var e = _entry(key);
        if (e == null) {
            long h = _hash(key);
            var values = dict.computeIfAbsent(h, k -> new ArrayList<>());
            e = new XEntry<K, V>(key, value);
            values.add(e);
            ++size;
        }
        e.value = value;
    }

    public void remove(K key) {
        long h = _hash(key);
        if (!dict.containsKey(h)) {
            return;
        }
        XEntry<K, V> e = null;
        for (var entry : dict.get(h)) {
            if (feat.isEqual(key, entry.key)) {
                e = entry;
                break;
            }
        }
        if (e != null) {
            dict.get(h).remove(e);
            --size;
        }
    }

    public int size() {
        return size;
    }

}
