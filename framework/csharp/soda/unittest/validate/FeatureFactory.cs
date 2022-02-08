namespace Soda.Unittest.Validate;

public static class FeatureFactory
{
    static FeatureFactory()
    {
        registerFactory<double>(() => new ObjectFeature<double>(
            (double d) => (long) d.GetHashCode(),
            (double a, double b) => Math.Abs(a - b) < 1e-6
        ));
    }

    private static readonly IDictionary<Type, Func<ObjectFeature>> factoryMap 
        = new Dictionary<Type, Func<ObjectFeature>>();

    private static void registerFactory<T>(Func<ObjectFeature<T>> factory)
    {
        factoryMap[typeof(T)] = factory;
    }

    public static ObjectFeature<object> create(Type type)
    {
        if (factoryMap.ContainsKey(type)) {
            return Utils.Cast<ObjectFeature<object>>(factoryMap[type]());
        }
        return new ObjectFeature<object>(
            (object obj) => obj.GetHashCode(), 
            (object a, object b) => object.Equals(a, b)
        );
    }

    public static ObjectFeature<T> create<T>()
    {
        var type = typeof(T);
        if (factoryMap.ContainsKey(type)) {
            return (ObjectFeature<T>) factoryMap[type]();
        }
        return new ObjectFeature<T>(
            (T obj) => (long) obj.GetHashCode(),
            (T a, T b) => object.Equals(a, b)
        );
    }
}