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
    vector<string> permutation(vector<char>& chars, int n) {
        vector<string> res;
        string buf(n, '\0');
        solve(chars, 0, buf, 0, res);
        return res;
    }

    void solve(vector<char>& chars, int i, string& buf, int j, vector<string>& res) {
        if (j == buf.size()) {
            res.push_back(buf);
            return;
        }
        for (int k = i; k < int(chars.size()); ++k) {
            std::swap(chars[i], chars[k]);
            buf[j] = chars[i];
            solve(chars, i+1, buf, j+1, res);
            std::swap(chars[i], chars[k]);
        }
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
    auto work = WorkFactory::create(su, &Solution::permutation);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);
    //
    // [3] or, create for struct tester
    // auto tester = WorkFactory::createStructTester<Class,Args...>();
    // ADD_FUNCTION(tester, funcname)
    // auto work = WorkFactory::forStruct(tester);

    work->setValidator(Validators::forVector<string>(false));
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
