package soda.unittest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Utils {

    public static String fromStdin() throws IOException {
        try (Scanner scan = new Scanner(System.in, StandardCharsets.UTF_8)) {
            StringBuilder buf = new StringBuilder();
            while (scan.hasNextLine()) {
                buf.append(scan.nextLine());
            }
            return buf.toString();
        }
    }

    public static String toString(Throwable ex) {
        var out = new ByteArrayOutputStream();
        var pw = new PrintStream(out);
        ex.printStackTrace(pw);
        pw.flush();
        return out.toString(StandardCharsets.UTF_8);
    }

}
