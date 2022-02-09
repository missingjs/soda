using System.Collections.Generic;

namespace Soda.Unittest.Validate;

public class XMap<K, V>
{
    private readonly ObjectFeature<K> feat;

    private IDictionary<long, IList<XEntry<K, V>>> dict = new Dictionary<long, IList<XEntry<K, V>>>();

    private int size;

    public int Size { get => size; }

    public XMap(ObjectFeature<K> feat)
    {
        this.feat = feat;
    }

    private long _hash(K key)
    {
        return feat.Hash(key);
    }

    public bool ContainsKey(K key)
    {
        return _entry(key) != null;
    }

    public V this[K key]
    {
        get {
            if (key == null) throw new ArgumentNullException();
            var e = _entry(key);
            if (e == null) throw new KeyNotFoundException();
            return e.Value;
        }
        set {
            var e = _entry(key);
            if (e == null) {
                long h = _hash(key);
                IList<XEntry<K, V>> values = null;
                if (!dict.ContainsKey(h)) {
                    dict[h] = values = new List<XEntry<K, V>>();
                }
                e = new XEntry<K, V>(key, value);
                values.Add(e);
                ++size;
            }
            e.Value = value;
        }
    }

    public V TryGetValue(K key, V defaultVal)
    {
        var e = _entry(key);
        return e != null ? e.Value : defaultVal;
    }

    public bool Remove(K key)
    {
        V value;
        return Remove(key, out value);
    }

    public bool Remove(K key, out V value)
    {
        long h = _hash(key);
        if (!dict.ContainsKey(h)) {
            value = default(V);
            return false;
        }
        XEntry<K, V> e = null;
        foreach (var entry in dict[h]) {
            if (feat.IsEqual(key, entry.Key)) {
                e = entry;
                break;
            }
        }
        if (e != null) {
            dict[h].Remove(e);
            --size;
            value = e.Value;
            return true;
        }
        value = default(V);
        return false;
    }

    private XEntry<K, V> _entry(K key)
    {
        long h = _hash(key);
        if (dict.ContainsKey(h)) {
            foreach (var entry in dict[h]) {
                if (feat.IsEqual(key, entry.Key)) {
                    return entry;
                }
            }
        }
        return null;
    }
}

class XEntry<K, V>
{
    public K Key { get; }

    public V Value { get; set; }

    public XEntry(K key, V value)
    {
        this.Key = key;
        this.Value = value;
    }
}