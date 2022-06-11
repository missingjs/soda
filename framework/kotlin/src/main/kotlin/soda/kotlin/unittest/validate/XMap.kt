package soda.kotlin.unittest.validate

class XEntry<K, V>(val key: K, var value: V)

class XMap<K, V>(private val feat: ObjectFeature<K>) {

    private val dict = mutableMapOf<Long, MutableList<XEntry<K, V>>>()

    private var size: Int = 0

    private fun _hash(key: K): Long = feat.hash(key)

    private fun _entry(key: K): XEntry<K, V>? {
        return dict.getOrElse(_hash(key)) { mutableListOf() }.find { feat.isEqual(key, it.key) }
    }

    operator fun contains(key: K): Boolean = _entry(key) != null

    operator fun get(key: K): V? = _entry(key)?.value

    fun getOrDefault(key: K, default: V): V {
        return _entry(key)?.value ?: default
    }

    operator fun set(key: K, value: V) {
        val e = _entry(key) ?: run {
            val h = _hash(key)
            val values = dict.getOrPut(h) { mutableListOf() }
            val entry = XEntry(key, value)
            values.add(entry)
            size += 1
            entry
        }
        e.value = value
    }

    operator fun minusAssign(key: K) {
        remove(key)
    }

    fun remove(key: K): V? {
        val h = _hash(key)
        var elem: V? = null
        dict[h]?.let { entries ->
            entries.find { e -> feat.isEqual(key, e.key) }?.let { entry ->
                dict[h]?.remove(entry)
                size -= 1
                elem = entry.value
            }
        }
        return elem
    }
}