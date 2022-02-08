namespace Soda.Unittest.Task;

public interface ITaskProxy<R>
{
    Type GetReturnType();

    Type[] GetArgumentTypes();

    object[] GetArguments();

    R Execute(WorkInput input);

    double GetElapseMillis();
}