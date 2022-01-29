package soda.unittest.task;

import soda.unittest.TestWork;
import soda.unittest.Utils;
import soda.unittest.WorkInput;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

abstract class TaskBase<R> implements TaskProxy<R> {

    private double elapseMillis;

    private final Type returnType;

    private final List<Type> argTypes;

    private List<Object> args;

    TaskBase(Class<?> workClass, String methodName) {
        var method = Utils.findMethod(workClass, methodName);
        var ats = method.getGenericParameterTypes();
        argTypes = Arrays.stream(ats).collect(Collectors.toList());
        var retType = method.getGenericReturnType();
        returnType = retType.equals(void.class) ? argTypes.get(0) : retType;
    }

    protected abstract R run();

    @SuppressWarnings("unchecked")
    protected <A> A arg(int index) {
        return (A) args.get(index);
    }

    @Override
    public R execute(WorkInput input) {
        args = Utils.parseArguments(argTypes, input.args);
        var startNano = System.nanoTime();
        var result = run();
        var endNano = System.nanoTime();
        elapseMillis = (endNano - startNano) / 1e6;
        return result;
    }

    @Override
    public Type getReturnType() {
        return returnType;
    }

    @Override
    public Type[] getArgumentTypes() {
        return argTypes.toArray(Type[]::new);
    }

    @Override
    public Object[] getArguments() {
        return args.toArray(Object[]::new);
    }

    @Override
    public double getElapseMillis() {
        return elapseMillis;
    }

}
