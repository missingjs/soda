using Soda.Unittest;
using Soda.Leetcode;


public class Logger {

    private IDictionary<string, int> msgMap = new Dictionary<string, int>();

    private const int limit = 10;

    private int lastTimestamp = -limit;

    public Logger() {
    }
    
    public bool ShouldPrintMessage(int timestamp, string message) {
        int T = lastTimestamp;
        lastTimestamp = timestamp;
        if (timestamp - T >= 10) {
            msgMap.Clear();
            msgMap[message] = timestamp;
            return true;
        }
        if (msgMap.ContainsKey(message) && timestamp - msgMap[message] < limit) {
            return false;
        }
        msgMap[message] = timestamp;
        return true;
    }
}

public class Lograte
{
    public static void Main(string[] args)
    {
        // var work = WorkFactory.Create(Utils.Fn(new Solution().Add));
        var work = WorkFactory.ForStruct<Logger>();
        // work.SetValidator((e, r) => ...);
        work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.ReadStdin()));
    }
}
