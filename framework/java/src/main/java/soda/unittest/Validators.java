package soda.unittest;

import soda.unittest.validate.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

public class Validators {

    private static <T> ObjectFeature<T> createFeature(Class<T> klass) {
        return FeatureFactory.create(klass);
    }

    public static <T> BiPredicate<List<T>, List<T>> forList(Class<T> klass, boolean ordered) {
        var f = createFeature(klass);
        return ordered ? StrategyFactory.list(f) : StrategyFactory.unorderList(f);
    }

    public static <T> BiPredicate<List<List<T>>, List<List<T>>> forList2d(Class<T> klass, boolean dim1Ordered, boolean dim2Ordered) {
        var elemFeat = createFeature(klass);
        var lf = dim2Ordered ? new ListFeature<T>(elemFeat) : new UnorderListFeature<T>(elemFeat);
        return dim1Ordered ? StrategyFactory.list(lf) : StrategyFactory.unorderList(lf);
    }

    public static <T> BiPredicate<T[], T[]> forArray(Class<T> klass, boolean ordered) {
        return (T[] a, T[] b) -> {
            var La = Validators.<T>toList(a);
            var Lb = Validators.<T>toList(b);
            return Validators.<T>forList(klass, ordered).test(La, Lb);
        };
    }

    public static <T> BiPredicate<T[][], T[][]> forArray2d(Class<T> klass, boolean dim1Ordered, boolean dim2Ordered) {
        return (T[][] a, T[][] b) -> {
            var La = Validators.<T>toList2d(a);
            var Lb = Validators.<T>toList2d(b);
            return Validators.<T>forList2d(klass, dim1Ordered, dim2Ordered).test(La, Lb);
        };
    }

    public static BiPredicate<int[], int[]> forIntArray(boolean ordered) {
        return (int[] a, int[] b) -> Validators.forList(Integer.class, ordered).test(box(a), box(b));
    }

    public static BiPredicate<int[][], int[][]> forIntArray2d(boolean dim1Ordered, boolean dim2Ordered) {
        return (int[][] a, int[][] b) -> Validators.forList2d(Integer.class, dim1Ordered, dim2Ordered).test(box(a), box(b));
    }

    public static BiPredicate<long[], long[]> forLongArray(boolean ordered) {
        return (long[] a, long[] b) -> Validators.forList(Long.class, ordered).test(box(a), box(b));
    }

    public static BiPredicate<long[][], long[][]> forLongArray2d(boolean dim1Ordered, boolean dim2Ordered) {
        return (long[][] a, long[][] b) -> Validators.forList2d(Long.class, dim1Ordered, dim2Ordered).test(box(a), box(b));
    }

    public static BiPredicate<double[], double[]> forDoubleArray(boolean ordered) {
        return (double[] a, double[] b) -> Validators.forList(Double.class, ordered).test(box(a), box(b));
    }

    public static BiPredicate<double[][], double[][]> forDoubleArray2d(boolean dim1Ordered, boolean dim2Ordered) {
        return (double[][] a, double[][] b) -> Validators.forList2d(Double.class, dim1Ordered, dim2Ordered).test(box(a), box(b));
    }

    private static List<Integer> box(int[] arr) {
        return Arrays.stream(arr).boxed().collect(Collectors.toList());
    }

    private static List<List<Integer>> box(int[][] arr2d) {
        return Arrays.stream(arr2d).map(Validators::box).collect(Collectors.toList());
    }

    private static List<Long> box(long[] arr) {
        return Arrays.stream(arr).boxed().collect(Collectors.toList());
    }

    private static List<List<Long>> box(long[][] arr2d) {
        return Arrays.stream(arr2d).map(Validators::box).collect(Collectors.toList());
    }

    private static List<Double> box(double[] arr) {
        return Arrays.stream(arr).boxed().collect(Collectors.toList());
    }

    private static List<List<Double>> box(double[][] arr2d) {
        return Arrays.stream(arr2d).map(Validators::box).collect(Collectors.toList());
    }

    private static <T> List<T> toList(T[] arr) {
        return Arrays.stream(arr).collect(Collectors.toList());
    }

    private static <T> List<List<T>> toList2d(T[][] arr2d) {
        return Arrays.stream(arr2d).map(Validators::toList).collect(Collectors.toList());
    }

}
