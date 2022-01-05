import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;

import static soda.unittest.LoggerHelper.logger;

class Logger {

    private Map<String, Integer> msgMap = new HashMap<>();

    private final int limit = 10;

    private int lastTimestamp = -limit;

    public Logger() {
    }
    
    public boolean shouldPrintMessage(int timestamp, String message) {
        int T = lastTimestamp;
        lastTimestamp = timestamp;
        if (timestamp - T >= 10) {
            msgMap.clear();
            msgMap.put(message, timestamp);
            return true;
        }
        if (msgMap.containsKey(message) && timestamp - msgMap.get(message) < limit) {
            return false;
        }
        msgMap.put(message, timestamp);
        return true;
    }
}

public class Lograte implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = TestWork.forStruct(Logger.class);
        // work.setValidator((e, r) -> {...});
        work.setCompareSerial(true);
        // work.setArgumentParser(index, a -> { ... });
        // work.setResultParser(r -> { ... });
        // work.setResultSerializer(r -> {...});
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Lograte().get().run();
    }

}
