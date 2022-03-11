import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Logger {

    private def msgMap = [:]

    private final int limit = 10

    private int lastTimestamp = -limit

    boolean shouldPrintMessage(int timestamp, String message) {
        int T = lastTimestamp
        lastTimestamp = timestamp
        if (timestamp - T >= 10) {
            msgMap.clear()
            msgMap[message] = timestamp
            return true
        }
        if (msgMap.containsKey(message) && timestamp - msgMap[message] < limit) {
            return false
        }
        msgMap[message] = timestamp
        true
    }
}

// def work = TestWork.create(new Solution().&add)
def work = TestWork.forStruct(Logger)
// work.validator = { i, j -> i == j }
work.compareSerial = true
println work.run(System.in.getText('UTF-8'))
