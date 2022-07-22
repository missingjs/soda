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
    vector<vector<double>> matrixMultiply(vector<vector<double>>& a, vector<vector<double>>& b) {
        int rows = int(a.size()), cols = int(b[0].size());
        vector<vector<double>> res(rows, vector<double>(cols));
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                double c = 0.0;
                for (int k = 0; k < int(b.size()); ++k) {
                    c += a[i][k] * b[k][j];
                }
                res[i][j] = c;
            }
        }
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
    auto work = WorkFactory::create(su, &Solution::matrixMultiply);
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
