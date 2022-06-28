package soda.unittest.task;

import soda.unittest.Utils;
import soda.unittest.function.Function3;

public class Task3<P1, P2, P3, R> extends TaskBase<R> {

    public Task3(Function3<P1, P2, P3, R> func) {
        super(func);
    }

    @Override
    protected R run() {
        return Utils.<Function3<P1, P2, P3, R>>cast(taskFunction)
                .apply(arg(0), arg(1), arg(2));
    }
}
