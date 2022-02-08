namespace Soda.Unittest.Validate;

public class ObjectFeature {}

public class ObjectFeature<T> : ObjectFeature
{
    private readonly Func<T, long> hashFunc;

    private readonly Func<T, T, bool> equalFunc;

    public ObjectFeature(Func<T, long> hs, Func<T, T, bool> eq)
    {
        hashFunc = hs;
        equalFunc = eq;
    }

    public long Hash(T obj) => hashFunc(obj);

    public bool IsEqual(T a, T b) => equalFunc(a, b);
}
