import soda.groovy.unittest.*

class Solution {
    public double[] calcEquation(List<List<String>> equations, double[] values, List<List<String>> queries) {
        def indexMap = getIndexMap(equations)
        int N = indexMap.size()
        def table = new double[N][N]
        for (line in table) {
            Arrays.fill(line, -1.0)
        }

        for (int k = 0; k < values.length; ++k) {
            def p = equations[k]
            int i = indexMap[p[0]]
            int j = indexMap[p[1]]
            table[i][j] = values[k]
            table[j][i] = 1.0 / values[k]
        }

        def res = new double[queries.size()]
        def visited = new boolean[N]
        for (int i = 0; i < res.length; ++i) {
            def a = queries[i][0]
            def b = queries[i][1]
            def ai = indexMap[a]
            def bi = indexMap[b]
            if (ai == null || bi == null) {
                res[i] = -1.0
                continue
            }
            if (ai == bi) {
                res[i] = 1.0
                continue
            }
            Arrays.fill(visited, false)
            res[i] = dfs(ai, bi, table, visited)
        }
        return res
    }

    private Map<String, Integer> getIndexMap(List<List<String>> eqs) {
        def imap = new HashMap<String, Integer>()
        for (e in eqs) {
            String a = e[0], b = e[1]
            if (!(a in imap)) {
                imap[a] = imap.size()
            }
            if (!(b in imap)) {
                imap[b] = imap.size()
            }
        }
        imap
    }

    private double dfs(int ai, int bi, double[][] table, boolean[] visited) {
        if (table[ai][bi] >= 0.0) {
            return table[ai][bi]
        }

        visited[ai] = true
        double res = -1.0
        for (int adj = 0; adj < table.length; ++adj) {
            if (table[ai][adj] >= 0.0 && !visited[adj]) {
                double v = dfs(adj, bi, table, visited)
                if (v >= 0.0) {
                    res = table[ai][adj] * v
                    break
                }
            }
        }
        table[ai][bi] = res
        table[bi][ai] = 1.0 / res
        res
    }
}

class LeetWork {
    String call(String input) {
        def work = TestWork.create(new Solution().&calcEquation)
        // def work = TestWork.forStruct(STRUCT)
        // work.validator = { i, j -> i == j }
        // work.compareSerial = true
        work.run(input)
    }
}

println new LeetWork()(System.in.getText('UTF-8'))
