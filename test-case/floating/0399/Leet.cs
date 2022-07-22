using Soda.Unittest;

public class Solution {
    public double[] CalcEquation(IList<IList<string>> equations, double[] values, IList<IList<string>> queries) {
        var indexMap = getIndexMap(equations);
        int N = indexMap.Count;
        var table = new double[N][];
        for (int i = 0; i < N; ++i) {
            table[i] = Enumerable.Repeat(-1.0, N).ToArray();
        }

        for (int k = 0; k < values.Length; ++k) {
            var p = equations[k];
            int i = indexMap[p[0]];
            int j = indexMap[p[1]];
            table[i][j] = values[k];
            table[j][i] = 1.0 / values[k];
        }

        var res = new double[queries.Count];
        var visited = new bool[N];
        for (int i = 0; i < res.Length; ++i) {
            var a = queries[i][0];
            var b = queries[i][1];
            if (!indexMap.ContainsKey(a) || !indexMap.ContainsKey(b)) {
                res[i] = -1.0;
                continue;
            }
            var ai = indexMap[a];
            var bi = indexMap[b];
            if (ai == bi) {
                res[i] = 1.0;
                continue;
            }
            Array.Fill(visited, false);
            res[i] = dfs(ai, bi, table, visited);
        }
        return res;
    }

    private IDictionary<string, int> getIndexMap(IList<IList<string>> eqs) {
        var imap = new Dictionary<string, int>();
        foreach (var e in eqs) {
            string a = e[0], b = e[1];
            if (!imap.ContainsKey(a)) {
                imap[a] = imap.Count;
            }
            if (!imap.ContainsKey(b)) {
                imap[b] = imap.Count;
            }
        }
        return imap;
    }

    private double dfs(int ai, int bi, double[][] table, bool[] visited) {
        if (table[ai][bi] >= 0.0) {
            return table[ai][bi];
        }

        visited[ai] = true;
        double res = -1.0;
        for (int adj = 0; adj < table.Length; ++adj) {
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
}

public class Leet
{
    public static void Main(string[] args)
    {
        var work = WorkFactory.Create(Utils.Fn(new Solution().CalcEquation));
        // work.SetValidator((e, r) => ...);
        // work.CompareSerial = true;
        Console.WriteLine(work.Run(Utils.readStdin()));
    }
}
