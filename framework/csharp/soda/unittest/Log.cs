namespace Soda.Unittest;

public static class Log
{
    public static void Info(string message)
    {
        Console.Error.WriteLine($"[INFO] {message}");
    }
}