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
    Solution(vector<int>& nums): original(nums) {
        srand(time(nullptr));
    }
    
    vector<int> reset() {
        return original;
    }
    
    vector<int> shuffle() {
        auto res = reset();
        for (int s = res.size(); s > 0; --s) {
            int i = rand() % s;
            int j = s - 1;
            if (i != j) {
                std::swap(res[i], res[j]);
            }
        }
        return res;
    }

private:
    vector<int> original;
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
    auto tester = WorkFactory::createStructTester<Solution,vector<int>>();
    ADD_FUNCTION(tester, reset)
    ADD_FUNCTION(tester, shuffle)
    auto work = WorkFactory::forStruct(tester);

    auto validator = [&](const JsonProxy& expect, const JsonProxy& result) {
        auto arguments = work->getArguments();
        auto commands = std::get<0>(arguments);
        for (int i = 1; i < commands.size(); ++i) {
            string cmd = commands[i];
            if (cmd == "shuffle") {
                auto evalues = expect[i].get<vector<int>>();
                auto rvalues = result[i].get<vector<int>>();
                if (!Validators::forVector<int>(false)) {
                    return false;
                }
            }
        }
        return true;
    };
    work->setValidator(validator);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
