package soda.unittest.validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnorderListFeature<T> implements ObjectFeature<List<T>> {

    private ObjectFeature<T> elemFeat;

    public UnorderListFeature(ObjectFeature<T> elemFeat) {
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
        return StrategyFactory.unorderList(elemFeat).test(a, b);
    }
}
