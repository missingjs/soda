using Newtonsoft.Json.Linq;
using Soda.Unittest.Conv;

namespace Soda.Unittest;

public static class Utils
{
    public static string ReadStdin()
    {
        using (var sr = new StreamReader(Console.OpenStandardInput(), Console.InputEncoding))
        {
            return sr.ReadToEnd();
        }
    }

    public static string readStdin()
    {
        return ReadStdin();
    }

    public static IList<object> ParseArguments(IList<Type> types, JArray rawParams)
    {
        return types.Select((t, i) => ConverterFactory.Create(t).FromJsonSerializable(rawParams[i])).ToList();
    }

    public static T Fn<T>(T t)
    {
        return t;
    }

    public static T Cast<T>(object obj)
    {
        return (T) obj;
    }

    public static string Capitalize(string s)
    {
        return s.Length > 0 ? (char.ToUpper(s[0]) + s.Substring(1)) : "";
    }
}