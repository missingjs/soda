package soda.unittest;

public class Log {

    public static void info(String format, Object... args) {
        var msg = String.format(format, args);
        System.err.println("[INFO] " + msg);
    }

}
