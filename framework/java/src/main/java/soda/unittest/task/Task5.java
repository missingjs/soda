package soda.unittest.task;

import soda.unittest.Utils;
import soda.unittest.function.Function5;

public class Task5<P1, P2, P3, P4, P5, R> extends TaskBase<R> {

    public Task5(Function5<P1, P2, P3, P4, P5, R> func) {
        super(func);
    }

    @Override
    protected R run() {
        return Utils.<Function5<P1, P2, P3, P4, P5, R>>cast(taskFunction)
                .apply(arg(0), arg(1), arg(2), arg(3), arg(4));
    }
}
