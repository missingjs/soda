using Newtonsoft.Json.Linq;

namespace Soda.Unittest.Conv;

public class ObjectConverter {}

public class ObjectConverter<T> : ObjectConverter
{
    private readonly Func<JToken, T> parser;

    private readonly Func<T, JToken> serializer;

    public ObjectConverter(Func<JToken, T> p, Func<T, JToken> s)
    {
        parser = p;
        serializer = s;
    }

    public T fromJsonSerializable(JToken js)
    {
        return parser(js);
    }

    public JToken toJsonSerializable(T obj)
    {
        return serializer(obj);
    }
}