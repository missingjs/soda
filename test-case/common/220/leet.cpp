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
    bool containsNearbyAlmostDuplicate(vector<int>& nums, int k, int t) {
        map<int64_t, int> imap;
        int i = 0, j = 0;
        while (j < int(nums.size())) {
            if (j - i <= k) {
                int64_t val = nums[j];
                ++j;
                int64_t lower = val - t, upper = val + t;
                auto iter = imap.lower_bound(lower);
                if (iter != imap.end() && iter->first <= upper) {
                    return true;
                }
                ++imap[val];
            } else {
                int64_t val = nums[i++];
                if (--imap[val] == 0) {
                    imap.erase(val);
                }
            }
        }
        return false;
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
    auto work = WorkFactory::create(su, &Solution::containsNearbyAlmostDuplicate);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);
    //
    // [3] or, create for struct tester
    // auto tester = WorkFactory::createStructTester<Class,Args...>();
    // ADD_FUNCTION(tester, funcname)
    // auto work = WorkFactory::forStruct(tester);
    //
    // [4] or, create for struct tester (simplified)
    // auto work = WorkFactory::forStruct<Class>();

    // work->setValidator(validate);
    work->setCompareSerial(true);
    cout << work->run(Utils::fromStdin());
    delete work;
    return 0;
}
