package soda.unittest.task;

import soda.unittest.Utils;
import soda.unittest.function.Function4;

public class Task4<P1, P2, P3, P4, R> extends TaskBase<R> {

    public Task4(Function4<P1, P2, P3, P4, R> func) {
        super(func);
    }

    @Override
    protected R run() {
        return Utils.<Function4<P1, P2, P3, P4, R>>cast(taskFunction)
                .apply(arg(0), arg(1), arg(2), arg(3));
    }
}