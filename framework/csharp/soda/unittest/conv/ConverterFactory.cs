using Newtonsoft.Json.Linq;

namespace Soda.Unittest.Conv;

public static class ConverterFactory
{
    private static readonly IDictionary<Type, Func<ObjectConverter>> factoryMap 
        = new Dictionary<Type, Func<ObjectConverter>>();

    private static void registerFactory(Type type, Func<ObjectConverter> factory)
    {
        factoryMap[type] = factory;
    }

    static ConverterFactory()
    {
        // TODO
    }

    public static ObjectConverter<Object> create(Type type)
    {
        if (factoryMap.ContainsKey(type)) {
            return Utils.Cast<ObjectConverter<Object>>(factoryMap[type]());
        }
        return new ObjectConverter<Object>(
            (JToken js) => js.ToObject(type), 
            (object obj) => JToken.FromObject(obj)
        );
    }

    public static ObjectConverter<T> create<T>(Type type)
    {
        return Utils.Cast<ObjectConverter<T>>(create(type));
    }
}