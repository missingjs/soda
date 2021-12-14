package soda.unittest.validate;

import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class DoubleArrayEqual implements BiPredicate<double[], double[]> {

    private final ListEqual<Double> tester;

    public DoubleArrayEqual() {
        tester = new ListEqual<>((d1, d2) -> Math.abs(d1-d2) < 1e-5);
    }

    @Override
    public boolean test(double[] a1, double[] a2) {
        var L1 = Arrays.stream(a1).boxed().collect(Collectors.toList());
        var L2 = Arrays.stream(a2).boxed().collect(Collectors.toList());
        return tester.test(L1, L2);
    }
}
