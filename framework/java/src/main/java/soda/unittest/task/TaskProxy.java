package soda.unittest.task;

import soda.unittest.WorkInput;

import java.lang.reflect.Type;

public interface TaskProxy<R> {

    Type getReturnType();

    Type[] getArgumentTypes();

    Object[] getArguments();

    R execute(WorkInput input);

    double getElapseMillis();

}
