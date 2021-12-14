package soda.unittest.validate;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class ArrayEqual<T> implements BiPredicate<T[], T[]> {

    private final ListEqual<T> listEqual;

    public ArrayEqual(BiPredicate<T,T> cmp) {
        this.listEqual = new ListEqual<T>(cmp);
    }

    @Override
    public boolean test(T[] t1, T[] t2) {
        var L1 = Arrays.stream(t1).collect(Collectors.toList());
        var L2 = Arrays.stream(t2).collect(Collectors.toList());
        return listEqual.test(L1, L2);
    }
}
