namespace Soda.Unittest.Validate;

public static class ListFeatureFactory
{
    public static ObjectFeature<IList<T>> Ordered<T>(ObjectFeature<T> elemFeat)
    {
        var hs = (IList<T> obj) =>
        {
            long res = 0L;
            foreach (var e in obj)
            {
                long h = elemFeat.Hash(e);
                res = res * 133 + h;
            }
            return res;
        };
        var eq = (IList<T> a, IList<T> b) =>
        {
            if (a.Count != b.Count)
            {
                return false;
            }
            for (int i = 0; i < a.Count; ++i)
            {
                if (!elemFeat.IsEqual(a[i], b[i]))
                {
                    return false;
                }
            }
            return true;
        };
        return new ObjectFeature<IList<T>>(hs, eq);
    }

    public static ObjectFeature<IList<T>> Unordered<T>(ObjectFeature<T> elemFeat)
    {
        var hs = (IList<T> obj) =>
        {
            var hashes = obj.Select(e => elemFeat.Hash(e)).ToList();
            hashes.Sort();
            long res = 0L;
            foreach (long h in hashes) {
                res = res * 133 + h;
            }
            return res;
        };
        var eq = (IList<T> a, IList<T> b) =>
        {
            if (a.Count != b.Count) {
                return false;
            }
            var xmap = new XMap<T, int>(elemFeat);
            foreach (var e in a) {
                xmap[e] = xmap.TryGetValue(e, 0) + 1;
            }
            foreach (var e in b) {
                if (!xmap.ContainsKey(e)) {
                    return false;
                }
                if (--xmap[e] == 0) {
                    xmap.Remove(e);
                }
            }
            return true;
        };
        return new ObjectFeature<IList<T>>(hs, eq);
    }
}

