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
class Solution {
public:
    vector<double> multiply(vector<double>& a, vector<double>& b) {
        vector<double> res;
        transform(a.begin(), a.end(), b.begin(), back_inserter(res), std::multiplies<>{});
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
    auto work = WorkFactory::create(su, &Solution::multiply);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);
    //
    // [3] or, create for struct tester
    // auto tester = WorkFactory::createStructTester<Class,Args...>();
    // ADD_FUNCTION(tester, funcname)
    // auto work = WorkFactory::forStruct(tester);

    // work->setValidator(Validators::forVector<double>(true));
    // work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
