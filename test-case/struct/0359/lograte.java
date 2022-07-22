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
            msgMap = new HashMap<>();
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
