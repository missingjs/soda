const {TestWork, Utils, Validators} = require('soda/unittest');

/**
 * @param {string[][]} equations
 * @param {number[]} values
 * @param {string[][]} queries
 * @return {number[]}
 */
var calcEquation = function(equations, values, queries) {
    let indexMap = getIndexMap(equations);
    let N = indexMap.size;
    let table = Array(N).fill(0).map(() => Array(N).fill(-1));

    for (let k = 0; k < values.length; ++k) {
        let p = equations[k];
        let i = indexMap.get(p[0]);
        let j = indexMap.get(p[1]);
        table[i][j] = values[k];
        table[j][i] = 1.0 / values[k];
    }

    let res = Array(queries.length).fill(0);
    let visited = Array(N).fill(false);
    for (let i = 0; i < res.length; ++i) {
        let [a, b] = queries[i];
        let ai = indexMap.get(a);
        let bi = indexMap.get(b);
        if (typeof ai == 'undefined' || typeof bi == 'undefined') {
            res[i] = -1;
            continue;
        }
        if (ai === bi) {
            res[i] = 1;
            continue;
        }
        visited.fill(false);
        res[i] = dfs(ai, bi, table, visited);
    }
    return res;
};

function getIndexMap(eqs) {
    let imap = new Map();
    for (let e of eqs) {
        let [a, b] = e;
        if (!imap.has(a)) {
            imap.set(a, imap.size);
        }
        if (!imap.has(b)) {
            imap.set(b, imap.size);
        }
    }
    return imap;
}

function dfs(ai, bi, table, visited) {
    if (table[ai][bi] >= 0) {
        return table[ai][bi];
    }
    visited[ai] = true;
    let res = -1;
    for (let adj = 0; adj < table.length; ++adj) {
        if (table[ai][adj] >= 0 && !visited[adj]) {
            let v = dfs(adj, bi, table, visited);
            if (v >= 0) {
                res = table[ai][adj] * v;
                break;
            }
        }
    }
    table[ai][bi] = res;
    table[bi][ai] = 1.0 / res;
    return res;
}

const work = TestWork.create(calcEquation);
// work.validator = (x, y) => { ... };
// work.compareSerial = true;
console.log(work.run(Utils.fromStdin()));

