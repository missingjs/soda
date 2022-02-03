package soda.unittest.validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnorderedListFeature<T> implements ObjectFeature<List<T>> {

    private ObjectFeature<T> elemFeat;

    public UnorderedListFeature(ObjectFeature<T> elemFeat) {
        this.elemFeat = elemFeat;
    }

    @Override
    public long hash(List<T> obj) {
        var hashes = new ArrayList<Long>();
        for (var e : obj) {
            hashes.add(elemFeat.hash(e));
        }
        Collections.sort(hashes);
        long res = 0;
        for (long h : hashes) {
            res = res * 133 + h;
        }
        return res;
    }

    @Override
    public boolean isEqual(List<T> a, List<T> b) {
        if (a.size() != b.size()) {
            return false;
        }
        var xmap = new XMap<T,Integer>(elemFeat);
        for (var e : a) {
            xmap.put(e, xmap.getOrDefault(e, 0) + 1);
        }
        for (var e : b) {
            if (!xmap.contains(e)) {
                return false;
            }
            int c = xmap.get(e) - 1;
            if (c == 0) {
                xmap.remove(e);
            } else {
                xmap.put(e, c);
            }
        }
        return true;
    }
}
