package soda.web;

import java.util.regex.Pattern;

public class Utils {

    public static String findOne(String text, String pattern, int group) {
        var mat = Pattern.compile(pattern).matcher(text);
        return mat.find() ? mat.group(group) : null;
    }

    public static String findOne(String text, String pattern) {
        return findOne(text, pattern, 1);
    }

}
