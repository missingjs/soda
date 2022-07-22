#ifdef __clang__
    #include "soda/clang/stdc++.h"
#else
    #include <bits/stdc++.h>
#endif
#include "soda/leetcode/leetcode.h"
#include "soda/unittest/unittest.h"

using namespace std;
using namespace soda::leetcode;
using namespace soda::unittest;

// step [1]: implement class Solution
// class Solution {};
class Solution {
public:
    vector<double> calcEquation(vector<vector<string>>& equations, vector<double>& values, vector<vector<string>>& queries) {
        unordered_map<string,int> indexMap;
        getIndexMap(equations, indexMap);
        int N = int(indexMap.size());
        vector<vector<double>> table(N, vector<double>(N, -1.0));

        for (int k = 0; k < int(values.size()); ++k) {
            auto& p = equations[k];
            int i = indexMap[p[0]], j = indexMap[p[1]];
            table[i][j] = values[k];
            table[j][i] = 1.0 / values[k];
        }

        vector<double> res(queries.size());
        vector<int> visited(N);
        for (int i = 0; i < int(res.size()); ++i) {
            auto a = queries[i][0];
            auto b = queries[i][1];
            if (indexMap.find(a) == indexMap.end() || indexMap.find(b) == indexMap.end()) {
                res[i] = -1.0;
                continue;
            }
            int ai = indexMap[a], bi = indexMap[b];
            if (ai == bi) {
                res[i] = 1.0;
                continue;
            }
            visited.assign(N, 0);
            res[i] = dfs(ai, bi, table, visited);
        }
        return res;
    }

    void getIndexMap(vector<vector<string>>& eqs, unordered_map<string,int>& imap) {
        for (auto& e : eqs) {
            string a = e[0], b = e[1];
            if (imap.find(a) == imap.end()) {
                imap[a] = imap.size();
            }
            if (imap.find(b) == imap.end()) {
                imap[b] = imap.size();
            }
        }
    }

    double dfs(int ai, int bi, vector<vector<double>>& table, vector<int>& visited) {
        if (table[ai][bi] >= 0.0) {
            return table[ai][bi];
        }

        visited[ai] = 1;
        double res = -1.0;
        for (int adj = 0; adj < int(table.size()); ++adj) {
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
};

namespace {
    const auto __ = []() {
        ios_base::sync_with_stdio(false);
        cin.tie(nullptr);
        return 0;
    }();
}

int main()
{
    // [1] create by class member function
    Solution su;
    auto work = WorkFactory::create(su, &Solution::calcEquation);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);
    //
    // [3] or, create for struct tester
    // auto tester = WorkFactory::createStructTester<Class,Args...>();
    // ADD_FUNCTION(tester, funcname)
    // auto work = WorkFactory::forStruct(tester);

    // work->setValidator(validate);
    // work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
