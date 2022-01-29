package soda.unittest.task;

import soda.unittest.function.Function4;

public class Task4<P1, P2, P3, P4, R> extends TaskBase<R> {

    private final Function4<P1, P2, P3, P4, R> func;

    public Task4(Class<?> workClass, String methodName, Function4<P1, P2, P3, P4, R> func) {
        super(workClass, methodName);
        this.func = func;
    }

    @Override
    protected R run() {
        return func.apply(arg(0), arg(1), arg(2), arg(3));
    }
}