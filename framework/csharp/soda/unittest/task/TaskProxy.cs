namespace soda.unittest.task;

public interface ITaskProxy<R>
{
    Type GetReturnType();

    Type[] GetArgumentTypes();

    object[] GetArguments();

    R execute(WorkInput input);

    double GetElapseMillis();
}