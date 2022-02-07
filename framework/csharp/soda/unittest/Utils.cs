namespace soda.unittest;

using System;
using System.IO;

public class Utils
{
    public static string readStdin()
    {
        using (var sr = new StreamReader(Console.OpenStandardInput(), Console.InputEncoding))
        {
            return sr.ReadToEnd();
        }
    }
}