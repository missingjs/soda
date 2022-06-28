package soda.unittest;

import soda.unittest.conv.ConverterFactory;
import soda.unittest.conv.ObjectConverter;
import soda.unittest.function.*;
import soda.unittest.task.*;
import soda.unittest.validate.FeatureFactory;

import java.util.Objects;
import java.util.function.BiPredicate;

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

    public static <P1, P2, P3, R> GenericTestWork<R> create(Function3<P1, P2, P3, R> func) {
        return new GenericTestWork<>(new Task3<>(func));
    }

    public static <P1, P2, P3, P4, R> GenericTestWork<R> create(
            Function4<P1, P2, P3, P4, R> func) {
        return new GenericTestWork<>(new Task4<>(func));
    }

    public static <P1, P2, P3, P4, P5, R> GenericTestWork<R> create(
            Function5<P1, P2, P3, P4, P5, R> func) {
        return new GenericTestWork<>(new Task5<>(func));
    }

    public static <P1, P2, P3, P4, P5, P6, R> GenericTestWork<R> create(
            Function6<P1, P2, P3, P4, P5, P6, R> func) {
        return new GenericTestWork<>(new Task6<>(func));
    }

    public static <P1, P2, P3, P4, P5, P6, P7, R> GenericTestWork<R> create(
            Function7<P1, P2, P3, P4, P5, P6, P7, R> func) {
        return new GenericTestWork<>(new Task7<>(func));
    }

    public static <P1, P2, P3, P4, P5, P6, P7, P8, R> GenericTestWork<R> create(
            Function8<P1, P2, P3, P4, P5, P6, P7, P8, R> func) {
        return new GenericTestWork<>(new Task8<>(func));
    }

    public static <P1, P2, P3, P4, P5, P6, P7, P8, P9, R> GenericTestWork<R> create(
            Function9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> func) {
        return new GenericTestWork<>(new Task9<>(func));
    }

    public static GenericTestWork<Object> forStruct(Class<?> structClass) {
        return create(new StructTester(structClass)::test);
    }

}
