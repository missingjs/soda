package soda.unittest.validate;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class LongArrayEqual implements BiPredicate<long[],long[]> {
    @Override
    public boolean test(long[] a1, long[] a2) {
        var L1 = Arrays.stream(a1).boxed().collect(Collectors.toList());
        var L2 = Arrays.stream(a2).boxed().collect(Collectors.toList());
        return Counting.elementCountMatches(L1, L2);
    }
}
