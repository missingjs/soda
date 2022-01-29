package soda.unittest.conv;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConvUtils {

    public static int[] toIntArray(List<Integer> L) {
        return L.stream().mapToInt(i -> i).toArray();
    }

    public static List<Integer> toList(int[] arr) {
        return Arrays.stream(arr).boxed().collect(Collectors.toList());
    }

    public static int[][] toIntArray2d(List<List<Integer>> L) {
        return L.stream().map(ConvUtils::toIntArray).toArray(int[][]::new);
    }

    public static List<List<Integer>> toList2d(int[][] arr) {
        return Arrays.stream(arr).map(ConvUtils::toList).collect(Collectors.toList());
    }

    public static char toChar(String s) {
        return s.charAt(0);
    }

    public static String toStr(char ch) {
        return "" + ch;
    }

    public static char[] toCharArray(List<String> ss) {
        return String.join("", ss).toCharArray();
    }

    public static List<String> toStrList(char[] chars) {
        return new String(chars).chars().mapToObj(c -> "" + (char) c).collect(Collectors.toList());
    }

    public static char[][] toCharArray2d(List<List<String>> ss) {
        return ss.stream().map(ConvUtils::toCharArray).toArray(char[][]::new);
    }

    public static List<List<String>> toStrList2d(char[][] ch2d) {
        return Arrays.stream(ch2d).map(ConvUtils::toStrList).collect(Collectors.toList());
    }

    public static List<Character> toCharList(List<String> ss) {
        return ss.stream().map(s -> s.charAt(0)).collect(Collectors.toList());
    }

    public static List<String> fromCharList(List<Character> chs) {
        return chs.stream().map(c -> "" + c).collect(Collectors.toList());
    }

    public static List<List<Character>> toCharList2d(List<List<String>> ss) {
        return ss.stream().map(ConvUtils::toCharList).collect(Collectors.toList());
    }

    public static List<List<String>> fromCharList2d(List<List<Character>> chs) {
        return chs.stream().map(ConvUtils::fromCharList).collect(Collectors.toList());
    }

    public static double[] toDoubleArray(List<Double> L) {
        return L.stream().mapToDouble(e -> e).toArray();
    }

    public static List<Double> toList(double[] arr) {
        return Arrays.stream(arr).boxed().collect(Collectors.toList());
    }

    public static double[][] toDoubleArray2d(List<List<Double>> L) {
        return L.stream().map(ConvUtils::toDoubleArray).toArray(double[][]::new);
    }

    public static List<List<Double>> toList2d(double[][] arr2d) {
        return Arrays.stream(arr2d).map(ConvUtils::toList).collect(Collectors.toList());
    }

    public static long[] toLongArray(List<Long> L) {
        return L.stream().mapToLong(e -> e).toArray();
    }

    public static List<Long> toList(long[] arr) {
        return Arrays.stream(arr).boxed().collect(Collectors.toList());
    }

    public static long[][] toLongArray2d(List<List<Long>> L) {
        return L.stream().map(ConvUtils::toLongArray).toArray(long[][]::new);
    }

    public static List<List<Long>> toList2d(long[][] arr2d) {
        return Arrays.stream(arr2d).map(ConvUtils::toList).collect(Collectors.toList());
    }

    public static String[] toStrArray(List<String> L) {
        return L.toArray(String[]::new);
    }

    public static List<String> toStrList(String[] arr) {
        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public static String[][] toStrArray2d(List<List<String>> L) {
        return L.stream().map(ConvUtils::toStrArray).toArray(String[][]::new);
    }

    public static List<List<String>> toStrList2d(String[][] arr2d) {
        return Arrays.stream(arr2d).map(ConvUtils::toStrList).collect(Collectors.toList());
    }

}
