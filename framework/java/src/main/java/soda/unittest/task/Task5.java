package soda.unittest.task;

import soda.unittest.function.Function5;

public class Task5<P1, P2, P3, P4, P5, R> extends TaskBase<R> {

    private final Function5<P1, P2, P3, P4, P5, R> func;

    public Task5(Class<?> workClass, String methodName, Function5<P1, P2, P3, P4, P5, R> func) {
        super(workClass, methodName);
        this.func = func;
    }

    @Override
    protected R run() {
        return func.apply(arg(0), arg(1), arg(2), arg(3), arg(4));
    }
}
