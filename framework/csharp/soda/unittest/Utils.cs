using Newtonsoft.Json.Linq;
using Soda.Unittest.Conv;

namespace Soda.Unittest;

public static class Utils
{
    public static string readStdin()
    {
        using (var sr = new StreamReader(Console.OpenStandardInput(), Console.InputEncoding))
        {
            return sr.ReadToEnd();
        }
    }

    public static IList<object> parseArguments(IList<Type> types, JArray rawParams)
    {
        return types.Select((t, i) => ConverterFactory.create(t).fromJsonSerializable(rawParams[i])).ToList();
    }

    public static T Fn<T>(T t)
    {
        return t;
    }

    public static T Cast<T>(object obj)
    {
        return (T) obj;
    }
}