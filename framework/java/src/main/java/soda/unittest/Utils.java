package soda.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Utils {

    public static final ObjectMapper objectMapper = new ObjectMapper();

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

    public interface SupplierEx<R> {
        R get() throws Throwable;
    }

    public interface RunEx {
        void run() throws Throwable;
    }

    public static void wrapEx(RunEx r) {
        wrapEx(() -> { r.run(); return 0; });
    }

    public static <R> R wrapEx(SupplierEx<R> r) {
        try {
            return r.get();
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }

    public static <T> T cast(Object obj) {
        @SuppressWarnings("unchecked")
        T t = (T) obj;
        return t;
    }

}
