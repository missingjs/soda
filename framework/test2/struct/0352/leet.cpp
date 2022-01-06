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
class SummaryRanges {
    vector<int> parent;
    unordered_set<int> ancestorSet;

public:
    SummaryRanges(): parent(10003), ancestorSet{} {
    }
    
    void addNum(int val) {
        ++val;
        if (parent[val]) {
            return;
        }

        parent[val] = -1;
        ancestorSet.insert(val);

        int left = val - 1, right = val + 1;
        if (left > 0 && parent[left]) {
            merge(left, val);
        }
        if (parent[right]) {
            merge(val, right);
        }
    }
    
    vector<vector<int>> getIntervals() {
        vector<int> ans(ancestorSet.begin(), ancestorSet.end());
        std::sort(ans.begin(), ans.end());
        vector<vector<int>> res(ans.size(), vector<int>(2));
        for (int i = 0; i < int(res.size()); ++i) {
            int start = ans[i];
            int end = start - parent[start] - 1;
            res[i][0] = start - 1;
            res[i][1] = end - 1;
        }
        return res;
    }

private:
    void merge(int x, int y) {
        int ax = getAncestor(x), ay = getAncestor(y);
        if (ax < ay) {
            mergeAncestor(ax, ay);
        } else {
            mergeAncestor(ay, ax);
        }
    }

    void mergeAncestor(int ax, int ay) {
        parent[ax] += parent[ay];
        parent[ay] = ax;
        ancestorSet.erase(ay);
    }

    int getAncestor(int x) {
        return parent[x] < 0 ? x : (parent[x] = getAncestor(parent[x]));
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
    // Solution su;
    // auto work = WorkFactory::create(su, &Solution::function);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);
    //
    // [3] or, create for struct tester
    auto tester = WorkFactory::createStructTester<SummaryRanges>();
    ADD_FUNCTION(tester, addNum)
    ADD_FUNCTION(tester, getIntervals)
    auto work = WorkFactory::forStruct(tester);

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
