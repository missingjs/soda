import soda.groovy.leetcode.*
import soda.groovy.unittest.*

class Solution {
    String[][] groupByLength(String[] strs) {
        def strList = strs.toList()
        Collections.shuffle(strList)
        def group = new HashMap<Integer, List<String>>()
        for (def s in strList) {
            group.computeIfAbsent(s.length(), k -> []).add(s);
        }
        def keys = group.keySet().toList()
        Collections.shuffle(keys)
        def res = new String[group.size()][]
        for (int i = 0; i < keys.size(); ++i) {
            res[i] = group[keys[i]].toArray(String[]::new)
        }
        res
    }
}

class List2dWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&groupByLength)
        // def work = TestWork.forStruct(STRUCT)
        work.validator = Validators.forArray2d(String, false, false)
        work.compareSerial = true
        work.run(input)
    }
}

println new List2dWork()(System.in.getText('UTF-8'))
