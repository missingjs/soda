package soda.unittest.validate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Counting {

    public static <T> boolean elementCountMatches(List<T> a, List<T> b) {
        var map = new HashMap<T,Integer>();
        for (var key : a) {
            map.put(key, map.getOrDefault(key, 0) + 1);
        }
        for (var key : b) {
            if (!map.containsKey(key)) {
                return false;
            }
            int c = map.get(key) - 1;
            if (c == 0) {
                map.remove(key);
            } else {
                map.put(key, c);
            }
        }
        return map.size() == 0;
    }

}
