package soda.unittest;

import soda.unittest.task.Function2;
import soda.unittest.task.Task1;
import soda.unittest.task.Task2;
import soda.unittest.task.TaskProxy;

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

    public static <P1, R> GenericTestWork<R> create1(Function<P1, R> func) { return null; }

    public static <P1, R> GenericTestWork<R> create1(Class<?> workClass, String methodName, Function<P1, R> func) {
        return new GenericTestWork<>(new Task1<>(workClass, methodName, func));
    }

    public static <P1> GenericTestWork<P1> create1u(Class<?> workClass, String methodName, Consumer<P1> func) {
        return create1(workClass, methodName, (P1 p1) -> { func.accept(p1); return p1; });
    }

    public static <P1, P2, R> GenericTestWork<R> create2(Class<?> workClass, String methodName, Function2<P1, P2, R> func) {
        return new GenericTestWork<>(new Task2<>(workClass, methodName, func));
    }

}
