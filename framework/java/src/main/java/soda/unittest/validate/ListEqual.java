package soda.unittest.validate;

import java.util.List;
import java.util.function.BiPredicate;

public class ListEqual<T> implements BiPredicate<List<T>,List<T>> {

    private final BiPredicate<T,T> elementTester;

    public ListEqual(BiPredicate<T,T> cmp) {
        this.elementTester = cmp;
    }

    @Override
    public boolean test(List<T> a, List<T> b) {
        if (a.size() != b.size()) {
            return false;
        }
        boolean[] visited = new boolean[a.size()];
        for (T t : a) {
            boolean exist = false;
            for (int i = 0; i < a.size(); ++i) {
                T p = b.get(i);
                if (!visited[i] && elementTester.test(t, p)) {
                    visited[i] = true;
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                return false;
            }
        }
        return true;
    }
}
