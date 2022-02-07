using System;
using System.IO;

using soda.unittest;

public class __Bootstrap__
{
    public static void Main(string[] args)
    {
        var work = new TestWork();
        Console.WriteLine(work.run(Utils.readStdin()));
    }
}
