import soda.kotlin.unittest.*

class Solution {
    fun calcEquation(equations: List<List<String>>, values: DoubleArray, queries: List<List<String>>): DoubleArray {
        val indexMap = getIndexMap(equations)
        val N = indexMap.size
        val table = Array(N) { DoubleArray(N) {-1.0} }

        for (k in values.indices) {
            val p = equations[k]
            val i = indexMap[p[0]] ?: -1
            val j = indexMap[p[1]] ?: -1
            table[i][j] = values[k]
            table[j][i] = 1.0 / values[k]
        }

        val res = DoubleArray(queries.size) {0.0}
        val visited = BooleanArray(N) {false}

        for (i in res.indices) {
            val a = queries[i][0]
            val b = queries[i][1]
            val ai = indexMap[a]
            val bi = indexMap[b]
            if (ai == null || bi == null) {
                res[i] = -1.0
            } else if (ai == bi) {
                res[i] = 1.0
            } else {
                for (j in visited.indices) {
                    visited[j] = false
                }
                res[i] = dfs(ai, bi, table, visited)
            }
        }

        return res
    }

    private fun getIndexMap(eqs: List<List<String>>): Map<String, Int> {
        val imap = mutableMapOf<String, Int>()
        for (e in eqs) {
            val a = e[0]
            val b = e[1]
            imap.getOrPut(a, {imap.size})
            imap.getOrPut(b, {imap.size})
        }
        return imap
    }

    private fun dfs(ai: Int, bi: Int, table: Array<DoubleArray>, visited: BooleanArray): Double {
        if (table[ai][bi] >= 0.0) {
            return table[ai][bi]
        }

        visited[ai] = true
        var res = -1.0
        for (adj in table.indices) {
            if (table[ai][adj] >= 0.0 && !visited[adj]) {
                val v = dfs(adj, bi, table, visited)
                if (v >= 0.0) {
                    res = table[ai][adj] * v
                    break
                }
            }
        }
        table[ai][bi] = res
        table[bi][ai] = 1.0 / res
        return res
    }
}

class Leet : (String) -> String {
    override fun invoke(text: String): String {
        // * setup with your method
        val work = GenericTestWork.create(Solution()::calcEquation)

        // * by method that has not return value
        // val work = GenericTestWork.createVoid(Solution.METHOD_WITHOUT_RETURN)
        // * by class of data struct
        // val work = GenericTestWork.forStruct(STRUCT::class)

        // * setup validator
        // work.validator = (T, T) -> Boolean
        // work.compareSerial = true
        return work.run(text)
    }

}

fun main() {
    print(Leet()(Utils.fromStdin()))
}
