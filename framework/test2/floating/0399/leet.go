package main

import (
    . "missingjs.com/soda/leetcode"
    "missingjs.com/soda/unittest"
    "missingjs.com/soda/util"
)

var logger = util.Logger
var _used ListNode

func calcEquation(equations [][]string, values []float64, queries [][]string) []float64 {
    var indexMap = getIndexMap(equations)
    var N = len(indexMap)
    var table = make([][]float64, N)
    for i := range table {
        table[i] = make([]float64, N)
        for j := range table[i] {
            table[i][j] = -1.0
        }
    }

    for k := 0; k < len(values); k++ {
        var p = equations[k]
        var i = indexMap[p[0]]
        var j = indexMap[p[1]]
        table[i][j] = values[k]
        table[j][i] = 1.0 / values[k]
    }

    var res = make([]float64, len(queries))
    var visited []bool

    for i := 0; i < len(res); i++ {
        var a = queries[i][0]
        var b = queries[i][1]
        ai, ok1 := indexMap[a]
        bi, ok2 := indexMap[b]
        if !ok1 || !ok2 {
            res[i] = -1.0
            continue
        }
        if ai == bi {
            res[i] = 1.0
            continue
        }
        visited = make([]bool, N)
        res[i] = dfs(ai, bi, table, visited)
    }
    return res
}

func getIndexMap(eqs [][]string) map[string]int {
    var imap = make(map[string]int)
    for _, e := range eqs {
        var a = e[0]
        var b = e[1]
        if _, ok := imap[a]; !ok {
            imap[a] = len(imap)
        }
        if _, ok := imap[b]; !ok {
            imap[b] = len(imap)
        }
    }
    return imap
}

func dfs(ai int, bi int, table [][]float64, visited []bool) float64 {
    if table[ai][bi] >= 0.0 {
        return table[ai][bi]
    }

    visited[ai] = true
    var res = -1.0
    for adj := 0; adj < len(table); adj++ {
        if table[ai][adj] >= 0.0 && !visited[adj] {
            var v = dfs(adj, bi, table, visited)
            if v >= 0.0 {
                res = table[ai][adj] * v
                break
            }
        }
    }
    table[ai][bi] = res
    table[bi][ai] = 1.0 / res
    return res
}

func main() {
    // create tester by function
    work := unittest.CreateWork(calcEquation)
    // OR create by struct factory
    // work := unittest.CreateWorkForStruct(Constructor)
    // work.SetValidator(func(e,r)bool)
    // work.CompareSerial = true
    work.Run()
}
