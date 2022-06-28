package soda.unittest;

import soda.unittest.conv.ConverterFactory;
import soda.unittest.conv.ObjectConverter;
import soda.unittest.function.*;
import soda.unittest.task.*;
import soda.unittest.validate.FeatureFactory;

import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericTestWork<R> {

    private final TaskProxy<R> proxy;

    private boolean compareSerial;

    private BiPredicate<R,R> validator;

    public GenericTestWork(TaskProxy<R> proxy) {
        this.proxy = proxy;
    }

    public String run(String text) {
        return Utils.wrapEx(() -> {
            var input = Utils.objectMapper.readValue(text, WorkInput.class);
            var result = proxy.execute(input);

            var output = new WorkOutput();
            output.id = input.id;
            output.elapse = proxy.getElapseMillis();

            var retType = proxy.getReturnType();
            ObjectConverter<R, Object> resConv = Utils.cast(ConverterFactory.createConverter(retType));
            output.result = resConv.toJsonSerializable(result);

            boolean success = true;
            if (input.expected != null) {
                if (compareSerial && validator == null) {
                    String a = Utils.objectMapper.writeValueAsString(input.expected);
                    String b = Utils.objectMapper.writeValueAsString(output.result);
                    success = Objects.equals(a, b);
                } else {
                    var expect = resConv.fromJsonSerializable(input.expected);
                    if (validator != null) {
                        success = validator.test(expect, result);
                    } else {
                        success = FeatureFactory.create(retType).isEqual(expect, result);
                    }
                }
            }
            output.success = success;
            return Utils.objectMapper.writeValueAsString(output);
        });
    }

    public void setCompareSerial(boolean b) {
        compareSerial = b;
    }

    public void setValidator(BiPredicate<R, R> va) {
        validator = va;
    }

    public Object[] getArguments() {
        return proxy.getArguments();
    }

    public static <T> GenericTestWork<T> create(SerialConsumer<T> func) {
        return new GenericTestWork<>(Task1.forVoid(func));
    }

    public static <P1, R> GenericTestWork<R> create(Function1<P1, R> func) {
        return new GenericTestWork<>(new Task1<>(func));
    }

    public static <P1, P2, R> GenericTestWork<R> create(Function2<P1, P2, R> func) {
        return new GenericTestWork<>(new Task2<>(func));
    }

    public static GenericTestWork<Object> forStruct(Class<?> structClass) {
        return create(new StructTester(structClass)::test);
    }

//    public static <P1, P2, R> GenericTestWork<R> create2(
//            Class<?> workClass, String methodName, Function2<P1, P2, R> func) {
//        return new GenericTestWork<>(new Task2<>(workClass, methodName, func));
//    }
//
//    public static <P1, P2, P3, R> GenericTestWork<R> create3(
//            Class<?> workClass, String methodName, Function3<P1, P2, P3, R> func) {
//        return new GenericTestWork<>(new Task3<>(workClass, methodName, func));
//    }
//
//    public static <P1, P2, P3, P4, R> GenericTestWork<R> create4(
//            Class<?> workClass, String methodName, Function4<P1, P2, P3, P4, R> func) {
//        return new GenericTestWork<>(new Task4<>(workClass, methodName, func));
//    }
//
//    public static <P1, P2, P3, P4, P5, R> GenericTestWork<R> create5(
//            Class<?> workClass, String methodName, Function5<P1, P2, P3, P4, P5, R> func) {
//        return new GenericTestWork<>(new Task5<>(workClass, methodName, func));
//    }
//
//    public static <P1, P2, P3, P4, P5, P6, R> GenericTestWork<R> create6(
//            Class<?> workClass, String methodName, Function6<P1, P2, P3, P4, P5, P6, R> func) {
//        return new GenericTestWork<>(new Task6<>(workClass, methodName, func));
//    }
//
//    // DO NOT use this !!! It's just a placeholder in bootstrap file
//    public static <P1, R> GenericTestWork<R> create1(Function1<P1, R> func) {
//        return create1(Solution.class, "METHOD", func);
//    }
//    public static <P1, P2, R> GenericTestWork<R> create2(Function2<P1, P2, R> func) {
//        return create2(Solution.class, "METHOD", func);
//    }

}
