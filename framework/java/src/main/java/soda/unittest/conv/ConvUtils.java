package soda.unittest.conv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
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

    public static List<List<Integer>> toList(int[][] arr) {
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

    public static List<List<String>> toStrList(char[][] ch2d) {
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

}
