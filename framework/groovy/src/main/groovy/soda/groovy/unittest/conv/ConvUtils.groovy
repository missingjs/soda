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

}
