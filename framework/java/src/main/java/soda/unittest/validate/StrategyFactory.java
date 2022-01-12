package soda.unittest.validate;

import java.util.List;
import java.util.function.BiPredicate;

public class StrategyFactory {

    public static <T> BiPredicate<List<T>, List<T>> unorderList(ObjectFeature<T> feat) {
        return (List<T> a, List<T> b) -> {
            if (a.size() != b.size()) {
                return false;
            }
            var xmap = new XMap<T,Integer>(feat);
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
        };
    }

    public static <T> BiPredicate<List<T>, List<T>> list(ObjectFeature<T> feat) {
        return (List<T> a, List<T> b) -> {
            if (a.size() != b.size()) {
                return false;
            }
            for (int i = 0; i < a.size(); ++i) {
                if (!feat.isEqual(a.get(i), b.get(i))) {
                    return false;
                }
            }
            return true;
        };
    }

}
