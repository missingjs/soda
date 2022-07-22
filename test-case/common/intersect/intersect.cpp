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
    vector<int> intersection(vector<int>& nums1, vector<int>& nums2) {
        if (nums1.size() > nums2.size()) {
            return intersection(nums2, nums1);
        }
        unordered_set<int> mset, res;
        for (int n : nums1) {
            mset.insert(n);
        }
        
        auto end = mset.end();
        for (int b : nums2) {
            if (mset.find(b) != end) {
                res.insert(b);
            }
        }
        return vector<int>(res.begin(), res.end());
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
    auto work = WorkFactory::create(su, &Solution::intersection);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);

    work->setValidator(Validators::forVector<int>(false));
    // work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
