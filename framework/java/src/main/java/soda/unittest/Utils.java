package soda.unittest;

import com.fasterxml.jackson.databind.ObjectMapper;
import soda.unittest.conv.ConverterFactory;
import soda.unittest.conv.ObjectConverter;
import soda.unittest.function.RunnableEx;
import soda.unittest.function.SupplierEx;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
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

    public static void wrapEx(RunnableEx r) {
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

    public static Object[] parseArguments(Type[] types, List<Object> rawParams) {
        Object[] arguments = new Object[types.length];
        for (int i = 0; i < arguments.length; ++i) {
            ObjectConverter<Object, Object> conv = cast(ConverterFactory.createConverter(types[i]));
            arguments[i] = conv.fromJsonSerializable(rawParams.get(i));
        }
        return arguments;
    }

    public static List<Object> parseArguments(List<Type> types, List<Object> rawParams) {
        var res = parseArguments(types.toArray(new Type[0]), rawParams);
        return Arrays.asList(res);
    }

    public static Method findMethod(Class<?> jobClass, String methodName) {
        Method[] methods = jobClass.getMethods();
        for (Method m : methods) {
            if (m.getName().equals(methodName)) {
                return m;
            }
        }
        throw new RuntimeException(new NoSuchMethodException(methodName));
    }

}
