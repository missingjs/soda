package soda.unittest;

import soda.unittest.function.*;
import soda.unittest.task.*;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

public class GenericTestWork<R> {

    private final TaskProxy<R> proxy;

    private final Validatable<R> validatable = new Validatable<>();

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
            validatable.validate(input, proxy.getReturnType(), result, output);
            return Utils.objectMapper.writeValueAsString(output);
        });
    }

    public void setCompareSerial(boolean b) {
        validatable.compareSerial = b;
    }

    public void setValidator(BiPredicate<R, R> va) {
        validatable.validator = va;
    }

    public Object[] getArguments() {
        return proxy.getArguments();
    }

    public static GenericTestWork<Object> forStruct(Class<?> structClass) {
        return create2(StructTester.class, "test", new StructTester(structClass)::test);
    }

    public static <P1, R> GenericTestWork<R> create1(
            Class<?> workClass, String methodName, Function<P1, R> func) {
        return new GenericTestWork<>(new Task1<>(workClass, methodName, func));
    }

    public static <P1> GenericTestWork<P1> create1u(
            Class<?> workClass, String methodName, Consumer<P1> func) {
        return create1(workClass, methodName, (P1 p1) -> { func.accept(p1); return p1; });
    }

    public static <P1, P2, R> GenericTestWork<R> create2(
            Class<?> workClass, String methodName, Function2<P1, P2, R> func) {
        return new GenericTestWork<>(new Task2<>(workClass, methodName, func));
    }

    public static <P1, P2, P3, R> GenericTestWork<R> create3(
            Class<?> workClass, String methodName, Function3<P1, P2, P3, R> func) {
        return new GenericTestWork<>(new Task3<>(workClass, methodName, func));
    }

    public static <P1, P2, P3, P4, R> GenericTestWork<R> create4(
            Class<?> workClass, String methodName, Function4<P1, P2, P3, P4, R> func) {
        return new GenericTestWork<>(new Task4<>(workClass, methodName, func));
    }

    public static <P1, P2, P3, P4, P5, R> GenericTestWork<R> create5(
            Class<?> workClass, String methodName, Function5<P1, P2, P3, P4, P5, R> func) {
        return new GenericTestWork<>(new Task5<>(workClass, methodName, func));
    }

    public static <P1, P2, P3, P4, P5, P6, R> GenericTestWork<R> create6(
            Class<?> workClass, String methodName, Function6<P1, P2, P3, P4, P5, P6, R> func) {
        return new GenericTestWork<>(new Task6<>(workClass, methodName, func));
    }

    // DO NOT use this !!! It's just a placeholder in bootstrap file
    public static <P1, R> GenericTestWork<R> create1(Function<P1, R> func) {
        return create1(Solution.class, "METHOD", func);
    }
    public static <P1, P2, R> GenericTestWork<R> create2(Function2<P1, P2, R> func) {
        return create2(Solution.class, "METHOD", func);
    }

}
