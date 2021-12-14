package soda.unittest;

import soda.unittest.validate.*;

import java.util.List;
import java.util.function.BiPredicate;

public class Validators {

    public static <T> BiPredicate<List<T>,List<T>> list1d() {
        return new ListEqual<>((a, b) -> a != null ? a.equals(b) : a == b);
    }

    public static <T> BiPredicate<List<List<T>>, List<List<T>>> list2d() {
        return new ListEqual<>(list1d());
    }

    public static <T> BiPredicate<T[], T[]> array1d() {
        return new ArrayEqual<>((a, b) -> a != null ? a.equals(b) : a == b);
    }

    public static <T> BiPredicate<T[][], T[][]> array2d() {
        return new ArrayEqual<>(array1d());
    }

    public static BiPredicate<int[], int[]> intArray1d() {
        return new IntArrayEqual();
    }

    public static BiPredicate<int[][], int[][]> intArray2d() {
        return new ArrayEqual<int[]>(intArray1d());
    }

    public static BiPredicate<long[], long[]> longArray1d() {
        return new LongArrayEqual();
    }

    public static BiPredicate<long[][], long[][]> longArray2d() {
        return new ArrayEqual<long[]>(longArray1d());
    }

    public static BiPredicate<double[], double[]> doubleArray1d() {
        return new DoubleArrayEqual();
    }

    public static BiPredicate<double[][], double[][]> doubleArray2d() {
        return new ArrayEqual<double[]>(doubleArray1d());
    }

}
