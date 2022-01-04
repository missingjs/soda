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
    string reverseVowels(string s) {
        vector<int> isv(128);
        string vs = "aeiouAEIOU";
        for (char ch : vs) {
            isv[ch] = 1;
        }
        int i = 0, j = int(s.size()-1);
        while (i < j) {
            while (i < j && !isv[s[i]]) {
                ++i;
            }
            while (i < j && !isv[s[j]]) {
                --j;
            }
            if (i < j) {
                std::swap(s[i], s[j]);
                ++i;
                --j;
            }
        }
        return s;
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
    auto work = WorkFactory::create(su, &Solution::reverseVowels);
    //
    // [2] or, create by ordinary function
    // auto work = WorkFactory::create(function);

    // work->setValidator(validate);
    work->setCompareSerial(true);
    work->run();
    delete work;
    return 0;
}
