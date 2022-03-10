package soda.groovy.unittest.conv

class ConvUtils {

    static char toChar(String s) {
        s[0] as char
    }

    static String fromChar(char ch) {
        ch
    }

    static char[] toCharArray(List<String> ss) {
        String.join("", ss).toCharArray()
    }

    static List<String> fromCharArray(char[] cc) {
        cc.collect({it as String})
    }


    static List<Character> toCharList(List<String> ss) {
        toCharArray(ss).toList()
    }

    static List<String> fromCharList(List<Character> cc) {
        cc.collect({fromChar(it)})
    }

    static char[][] toCharArray2d(List<List<String>> ss2d) {
        ss2d.collect({ toCharArray(it)}).toArray(char[][]::new)
    }

    static List<List<String>> fromCharArray2d(char[][] cc2d) {
        cc2d.collect({fromCharArray(it)})
    }

    static List<List<Character>> toCharList2d(List<List<String>> ss2d) {
        ss2d.collect({toCharList(it)})
    }

    static List<List<String>> fromCharList2d(List<List<Character>> cc2d) {
        cc2d.collect({fromCharList(it)})
    }

    static int[] toIntArray(List<Integer> ints) {
        ints
    }

    static List<Integer> fromIntArray(int[] ints) {
        ints.toList()
    }

    static int[][] toIntArray2d(List<List<Integer>> ints2d) {
        ints2d.collect({ toIntArray(it) }).toArray(int[][]::new)
    }

    static List<List<Integer>> fromIntArray2d(int[][] ints2d) {
        ints2d.collect({ fromIntArray(it) })
    }

    static double[] toDoubleArray(List<Double> ds) {
        ds
    }

    static List<Double> fromDoubleArray(double[] ds) {
        ds.toList()
    }

    static double[][] toDoubleArray2d(List<List<Double>> ds2d) {
        ds2d.collect({ toDoubleArray(it) }).toArray(double[][]::new)
    }

    static List<List<Double>> fromDoubleArray2d(double[][] ds2d) {
        ds2d.collect({ fromDoubleArray(it) })
    }

}
