package soda.unittest.task;

import soda.unittest.Utils;
import soda.unittest.function.Function9;

public class Task9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> extends TaskBase<R> {

    public Task9(Function9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R> func) {
        super(func);
    }

    @Override
    protected R run() {
        return Utils.<Function9<P1, P2, P3, P4, P5, P6, P7, P8, P9, R>>cast(taskFunction)
                .apply(arg(0), arg(1), arg(2), arg(3),
                        arg(4), arg(5), arg(6), arg(7), arg(8)
                );
    }
}
