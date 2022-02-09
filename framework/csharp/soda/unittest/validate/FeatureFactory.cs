namespace Soda.Unittest.Validate;

public static class FeatureFactory
{
    static FeatureFactory()
    {
        // double
        registerFactory(() => new ObjectFeature<double>(
            (double d) => (long) d.GetHashCode(),
            (double a, double b) => Math.Abs(a - b) < 1e-6
        ));

        // IList<double>
        registerFactory(() => ListFeatureFactory.Ordered<double>(create<double>()));

        // double[]
        registerFactory(() => {
            var proxy = create<IList<double>>();
            return new ObjectFeature<double[]>(
                (double[] arr) => proxy.Hash(arr),
                (double[] x, double[] y) => proxy.IsEqual(x, y)
            );
        });

        // IList<IList<double>>
        registerFactory(() => ListFeatureFactory.Ordered<IList<double>>(create<IList<double>>()));

        // double[][]
        registerFactory(() => {
            var proxy = create<IList<IList<double>>>();
            return new ObjectFeature<double[][]>(
                (double[][] arr2d) => proxy.Hash(arr2d),
                (double[][] x, double[][] y) => proxy.IsEqual(x, y)
            );
        });
    }

    private static readonly IDictionary<Type, Func<ObjectFeature>> factoryMap 
        = new Dictionary<Type, Func<ObjectFeature>>();

    private static void registerFactory<T>(Func<ObjectFeature<T>> factory)
    {
        factoryMap[typeof(T)] = factory;
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