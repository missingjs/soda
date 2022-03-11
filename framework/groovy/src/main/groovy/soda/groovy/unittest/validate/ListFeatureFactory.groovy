package soda.groovy.unittest.validate

class ListFeatureFactory {

    static <T> ObjectFeature<List<T>> ordered(ObjectFeature<T> elemFeat) {
        def hashFunc = { List<T> obj ->
            long res = 0
            for (e in obj) {
                long h = elemFeat.hash(e)
                res = res * 133 + h
            }
            res
        }
        def equalFunc = { List<T> a, List<T> b ->
            if (a.size() != b.size()) {
                return false
            }
            for (int i = 0; i < a.size(); ++i) {
                if (!elemFeat.isEqual(a[i], b[i])) {
                    return false
                }
            }
            return true
        }
        return new ObjectFeature<List<T>>(hashFunc, equalFunc)
    }

    static <T> ObjectFeature<List<T>> unordered(ObjectFeature<T> elemFeat) {
        def hashFunc = { List<T> obj ->
            def hashes = obj.collect { T e -> elemFeat.hash(e) }
            hashes.sort(true)
            long res = 0
            for (h in hashes) {
                res = res * 133 + h
            }
            res
        }

        def equalFunc = { List<T> a, List<T> b ->
            if (a.size() != b.size()) {
                return false
            }
            def xmap = new XMap<T, Integer>(elemFeat)
            for (e in a) {
                xmap.put(e, xmap.getOrDefault(e, 0) + 1)
            }
            for (e in b) {
                if (!xmap.containsKey(e)) {
                    return false
                }
                int c = xmap.get(e) - 1
                if (c == 0) {
                    xmap.remove(e)
                } else {
                    xmap.put(e, c)
                }
            }
            return true
        }

        return new ObjectFeature<List<T>>(hashFunc, equalFunc)
    }

}
