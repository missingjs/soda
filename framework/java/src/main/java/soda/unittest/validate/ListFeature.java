package soda.unittest.validate;

import java.util.List;

public class ListFeature<T> implements ObjectFeature<List<T>> {

    private ObjectFeature<T> elemFeat;

    public ListFeature(ObjectFeature<T> elemFeat) {
        this.elemFeat = elemFeat;
    }

    @Override
    public long hash(List<T> obj) {
        long res = 0L;
        for (var e : obj) {
            long h = elemFeat.hash(e);
            res = res * 133 + h;
        }
        return res;
    }

    @Override
    public boolean isEqual(List<T> a, List<T> b) {
        return StrategyFactory.list(elemFeat).test(a, b);
    }
}
