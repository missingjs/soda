namespace Soda.Unittest.Validate;

public static class FeatureFactory
{
    static FeatureFactory()
    {
        // TODO
    }

    private static readonly IDictionary<Type, Func<ObjectFeature>> factoryMap 
        = new Dictionary<Type, Func<ObjectFeature>>();

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

    public static ObjectFeature<T> create<T>(Type type)
    {
        return Utils.Cast<ObjectFeature<T>>(create(type));
    }
}