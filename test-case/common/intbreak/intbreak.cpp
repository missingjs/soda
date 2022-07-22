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
    vector<int> memo;
public:
    int integerBreak(int n) {
        memo.assign(59, 0);
        return solve(n);
    }

private:
    int solve(int n) {
        if (n == 1) {
            return 1;
        }
        if (memo[n] > 0) {
            return memo[n];
        }
        int res = 0;
        for (int i = 1; i < n; ++i) {
            res = std::max(i * (n-i), res);
            res = std::max(i * solve(n-i), res);
        }
        memo[n] = res;
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
    auto work = WorkFactory::create(su, &Solution::integerBreak);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
