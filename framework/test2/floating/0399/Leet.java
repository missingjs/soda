import soda.unittest.*;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.*;

import soda.leetcode.*;
import soda.unittest.Validators;
import soda.unittest.work.TestWork;


class Solution {
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        return method2(equations, values, queries);
    }

    public double[] method2(List<List<String>> equations, double[] values, List<List<String>> queries) {
        var indexMap = getIndexMap(equations);
        int N = indexMap.size();
        var table = new double[N][N];
        for (var line : table) {
            Arrays.fill(line, -1.0);
        }

        for (int k = 0; k < values.length; ++k) {
            var p = equations.get(k);
            int i = indexMap.get(p.get(0));
            int j = indexMap.get(p.get(1));
            table[i][j] = values[k];
            table[j][i] = 1.0 / values[k];
        }

        var res = new double[queries.size()];
        var visited = new boolean[N];
        for (int i = 0; i < res.length; ++i) {
            var a = queries.get(i).get(0);
            var b = queries.get(i).get(1);
            var ai = indexMap.get(a);
            var bi = indexMap.get(b);
            if (ai == null || bi == null) {
                res[i] = -1.0;
                continue;
            }
            if (ai.equals(bi)) {
                res[i] = 1.0;
                continue;
            }
            Arrays.fill(visited, false);
            res[i] = dfs(ai, bi, table, visited);
        }
        return res;
    }

    private Map<String, Integer> getIndexMap(List<List<String>> eqs) {
        var imap = new HashMap<String, Integer>();
        for (var e : eqs) {
            String a = e.get(0), b = e.get(1);
            if (!imap.containsKey(a)) {
                imap.put(a, imap.size());
            }
            if (!imap.containsKey(b)) {
                imap.put(b, imap.size());
            }
        }
        return imap;
    }

    private double dfs(int ai, int bi, double[][] table, boolean[] visited) {
        if (table[ai][bi] >= 0.0) {
            return table[ai][bi];
        }

        visited[ai] = true;
        double res = -1.0;
        for (int adj = 0; adj < table.length; ++adj) {
            if (table[ai][adj] >= 0.0 && !visited[adj]) {
                double v = dfs(adj, bi, table, visited);
                if (v >= 0.0) {
                    res = table[ai][adj] * v;
                    break;
                }
            }
        }
        table[ai][bi] = res;
        table[bi][ai] = 1.0 / res;
        return res;
    }

    public double[] method1(List<List<String>> equations, double[] values, List<List<String>> queries) {
        var variables = new HashSet<String>();
        var table = new HashMap<String, Map<String, Double>>();
        for (int i = 0; i < values.length; ++i) {
            String a = equations.get(i).get(0), b = equations.get(i).get(1);
            table.computeIfAbsent(a, _k -> new HashMap<>()).put(b, values[i]);
            table.computeIfAbsent(b, _k -> new HashMap<>()).put(a, 1.0 / values[i]);
            variables.add(a);
            variables.add(b);
        }

        var res = new double[queries.size()];
        for (int i = 0; i < res.length; ++i) {
            var q = queries.get(i);
            String a = q.get(0), b = q.get(1);
            if (!variables.contains(a) || !variables.contains(b)) {
                res[i] = -1.0;
            } else {
                var seen = new HashSet<String>();
                res[i] = calc(a, b, table, seen);
            }
        }
        return res;
    }

    private double calc(String a, String b, Map<String, Map<String, Double>> table, Set<String> seen) {
        if (a.equals(b)) {
            return 1.0;
        }

        var t = table.get(a);

        seen.add(a);
        var res = t.get(b);

        if (res == null) {
            res = -1.0;
            for (var entry : t.entrySet()) {
                if (!seen.contains(entry.getKey()) && entry.getValue() >= 0.0) {
                    var v = calc(entry.getKey(), b, table, seen);
                    if (v >= 0.0) {
                        res = entry.getValue() * v;
                        break;
                    }
                }
            }
            table.get(a).put(b, res);
        }

        seen.remove(a);

        return res;
    }
}

public class Leet implements Supplier<TestWork> {

    @Override
    public TestWork get() {
        var work = new TestWork(new Solution(), "calcEquation");
        // var work = TestWork.forStruct(Struct.class);
        // work.setValidator((e, r) -> {...});
        // work.setCompareSerial(true);
        return work;
    }

    public static void main(String[] args) throws Exception {
        new Leet().get().run();
    }

}
